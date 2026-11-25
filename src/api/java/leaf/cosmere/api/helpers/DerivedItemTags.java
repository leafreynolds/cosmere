package leaf.cosmere.api.helpers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

// Thanks to ProjectE's EMC mapper for inspiration on how to handle this
/**
 * Adds items to tags like {@link CosmereTags.Items#CONTAINS_METAL} when they are crafted (at any depth) from something
 * already in the tag. Runs on the server after every data load; the result is bound into the real tags, so clients
 * receive it with the normal tag sync and {@code stack.is(tag)} works everywhere.
 * <p>
 * Recipes are read through {@link Recipe#getIngredients()}. If your recipe type stores its inputs elsewhere,
 * register a reader during mod construction:
 * <pre>{@code DerivedItemTags.registerIngredientReader(MyRecipe.class, r -> r.inputs());}</pre>
 */
public final class DerivedItemTags
{
	// excluded: datapack tag of items never derived into this tag, even when crafted from something in it
	private record TrackedTag(TagKey<Item> items, TagKey<Block> blocks, TagKey<Item> excluded) {}

	private record Conversion(Item result, List<Set<Item>> slots) {}

	private static final List<TrackedTag> TRACKED = List.of(
			new TrackedTag(CosmereTags.Items.CONTAINS_METAL, CosmereTags.Blocks.CONTAINS_METAL, CosmereTags.Items.CONTAINS_METAL_EXCLUSIONS),
			new TrackedTag(CosmereTags.Items.CONTAINS_ALUMINUM, CosmereTags.Blocks.CONTAINS_ALUMINUM, CosmereTags.Items.CONTAINS_ALUMINUM_EXCLUSIONS));

	private static final Map<Class<?>, Function<Recipe<?>, List<Ingredient>>> READERS = new LinkedHashMap<>();

	static
	{
		// smithing recipes report no ingredients and keep them in package-private fields, so rebuild them from the public predicates
		registerIngredientReader(SmithingTransformRecipe.class, r -> List.of(
				ingredientMatching(r::isTemplateIngredient),
				ingredientMatching(r::isBaseIngredient),
				ingredientMatching(r::isAdditionIngredient)));
	}

	private DerivedItemTags() {}

	@SuppressWarnings("unchecked")
	public static synchronized <R extends Recipe<?>> void registerIngredientReader(Class<R> type, Function<R, List<Ingredient>> reader)
	{
		READERS.put(type, recipe -> reader.apply((R) recipe));
	}

	/** Called after tags are bound on a server data load (world start, /reload). */
	public static synchronized void inject(RecipeManager recipeManager, RegistryAccess registryAccess)
	{
		final List<Conversion> conversions = readRecipes(recipeManager, registryAccess);

		Map<TagKey<Item>, Collection<? extends Holder<Item>>> itemAdditions = new HashMap<>();
		Map<TagKey<Block>, Collection<? extends Holder<Block>>> blockAdditions = new HashMap<>();
		for (TrackedTag tracked : TRACKED)
		{
			final Set<Item> found = propagate(tracked, conversions);
			itemAdditions.put(tracked.items(), found.stream().map(BuiltInRegistries.ITEM::wrapAsHolder).toList());
			blockAdditions.put(tracked.blocks(), found.stream()
					.filter(item -> item instanceof BlockItem)
					.map(item -> BuiltInRegistries.BLOCK.wrapAsHolder(((BlockItem) item).getBlock()))
					.toList());
		}

		addToTags(BuiltInRegistries.ITEM, itemAdditions);
		addToTags(BuiltInRegistries.BLOCK, blockAdditions);
	}

	private static List<Conversion> readRecipes(RecipeManager recipeManager, RegistryAccess registryAccess)
	{
		List<Conversion> conversions = new ArrayList<>();
		Map<String, Integer> unreadable = new TreeMap<>();

		for (Recipe<?> recipe : recipeManager.getRecipes())
		{
			try
			{
				// no isSpecial() skip: Create marks every processing recipe special just to hide it from the recipe book.
				// Truly dynamic recipes (vanilla/cosmere CustomRecipe) report no result and no ingredients, so they drop out below anyway.
				final ItemStack resultStack = recipe.getResultItem(registryAccess);
				// we have one confirmed bug report that the result _can_ be null https://github.com/leafreynolds/cosmere/issues/58
				if (resultStack == null || resultStack.isEmpty())
				{
					continue;
				}

				final List<Ingredient> ingredients = ingredientsOf(recipe);
				if (ingredients.isEmpty())
				{
					unreadable.merge(String.valueOf(BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType())), 1, Integer::sum);
					continue;
				}

				List<Set<Item>> slots = new ArrayList<>();
				for (Ingredient ingredient : ingredients)
				{
					Set<Item> options = new HashSet<>();
					for (ItemStack option : ingredient.getItems())
					{
						// a returned container or crafting tool isn't consumed into the result
						if (!option.hasCraftingRemainingItem())
						{
							options.add(option.getItem());
						}
					}
					slots.add(options);
				}
				conversions.add(new Conversion(resultStack.getItem(), slots));
			}
			catch (Exception e)
			{
				// a broken modded recipe or ingredient must not take down world load or /reload
				CosmereAPI.logger.error("[DerivedTags] Skipping recipe {}, it crashed while reading its ingredients. Please report this to {}.", recipe.getId(), recipe.getId().getNamespace(), e);
			}
		}

		// in-world conversions with no recipe: copper weathers and is scraped back, waxing and unwaxing (the wax is never metal)
		WeatheringCopper.NEXT_BY_BLOCK.get().forEach((block, next) -> addTwoWay(conversions, block, next));
		HoneycombItem.WAXABLES.get().forEach((block, waxed) -> addTwoWay(conversions, block, waxed));

		if (!unreadable.isEmpty())
		{
			CosmereAPI.logger.info("[DerivedTags] Recipes skipped because no ingredients could be read (register a reader with DerivedItemTags.registerIngredientReader): {}", unreadable);
		}
		return conversions;
	}

	// an item is in the tag if any recipe for it has any ingredient option already in the tag; repeat until nothing new is found
	private static Set<Item> propagate(TrackedTag tracked, List<Conversion> conversions)
	{
		final TagKey<Item> tag = tracked.items();
		Set<Item> excluded = new HashSet<>();
		BuiltInRegistries.ITEM.getTagOrEmpty(tracked.excluded()).forEach(holder -> excluded.add(holder.value()));

		Set<Item> found = new HashSet<>();
		BuiltInRegistries.ITEM.getTagOrEmpty(tag).forEach(holder -> found.add(holder.value()));

		boolean grew;
		do
		{
			grew = false;
			for (Conversion conversion : conversions)
			{
				if (found.contains(conversion.result()) || excluded.contains(conversion.result()))
				{
					continue;
				}
				final Optional<Item> via = conversion.slots().stream().flatMap(Set::stream).filter(found::contains).findFirst();
				if (via.isPresent())
				{
					found.add(conversion.result());
					grew = true;
					CosmereAPI.logger.info("[DerivedTags] {}: {} added, thanks to {} in recipe",
							tag.location(), BuiltInRegistries.ITEM.getKey(conversion.result()), BuiltInRegistries.ITEM.getKey(via.get()));
				}
			}
		}
		while (grew);

		return found;
	}

	private static List<Ingredient> ingredientsOf(Recipe<?> recipe)
	{
		return READERS.entrySet().stream()
				.filter(entry -> entry.getKey().isInstance(recipe))
				.map(Map.Entry::getValue)
				.findFirst()
				.orElse(Recipe::getIngredients)
				.apply(recipe);
	}

	private static Ingredient ingredientMatching(Predicate<ItemStack> predicate)
	{
		return Ingredient.of(BuiltInRegistries.ITEM.stream()
				.map(Item::getDefaultInstance)
				.filter(stack -> !stack.isEmpty() && predicate.test(stack)));
	}

	// same call vanilla makes when it loads tags, with our holders appended
	private static <T> void addToTags(Registry<T> registry, Map<TagKey<T>, Collection<? extends Holder<T>>> additions)
	{
		Map<TagKey<T>, List<Holder<T>>> tags = new HashMap<>();
		registry.getTags().forEach(pair -> tags.put(pair.getFirst(), pair.getSecond().stream().toList()));
		additions.forEach((tag, holders) ->
		{
			Set<Holder<T>> merged = new LinkedHashSet<>(tags.getOrDefault(tag, List.of()));
			merged.addAll(holders);
			tags.put(tag, List.copyOf(merged));
		});
		registry.bindTags(tags);
	}

	private static void addTwoWay(List<Conversion> conversions, Block a, Block b)
	{
		if (a.asItem() == Items.AIR || b.asItem() == Items.AIR)
		{
			return;
		}
		conversions.add(new Conversion(b.asItem(), List.of(Set.of(a.asItem()))));
		conversions.add(new Conversion(a.asItem(), List.of(Set.of(b.asItem()))));
	}
}
