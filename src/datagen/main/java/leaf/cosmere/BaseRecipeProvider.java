/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.DifferenceIngredient;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class BaseRecipeProvider extends RecipeProvider
{

	private final ExistingFileHelper existingFileHelper;
	private final String modid;

	protected BaseRecipeProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modid)
	{
		super(output);
		this.existingFileHelper = existingFileHelper;
		this.modid = modid;
	}

	@Override
	protected final void buildRecipes(Consumer<FinishedRecipe> consumer)
	{
		Consumer<FinishedRecipe> trackingConsumer = consumer.andThen(recipe ->
				existingFileHelper.trackGenerated(recipe.getId(), PackType.SERVER_DATA, ".json", "recipes"));
		addRecipes(trackingConsumer);
	}

	protected abstract ResourceLocation makeRL(String path);

	protected abstract void addRecipes(Consumer<FinishedRecipe> consumer);

	public static Ingredient createIngredient(TagKey<Item> itemTag, ItemLike... items)
	{
		return createIngredient(Collections.singleton(itemTag), items);
	}

	public static Ingredient createIngredient(Collection<TagKey<Item>> itemTags, ItemLike... items)
	{
		return Ingredient.fromValues(Stream.concat(
				itemTags.stream().map(Ingredient.TagValue::new),
				Arrays.stream(items).map(item -> new Ingredient.ItemValue(new ItemStack(item)))
		));
	}

	@SafeVarargs
	public static Ingredient createIngredient(TagKey<Item>... tags)
	{
		return Ingredient.fromValues(Arrays.stream(tags).map(Ingredient.TagValue::new));
	}

	public static Ingredient difference(TagKey<Item> base, ItemLike subtracted)
	{
		return DifferenceIngredient.of(Ingredient.of(base), Ingredient.of(subtracted));
	}

	protected void addOreSmeltingRecipes(Consumer<FinishedRecipe> consumer, ItemLike ore, Item result, float experience, int time)
	{
		String name = RegistryHelper.get(result).getPath();
		String path = RegistryHelper.get(ore.asItem()).getPath();
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ore), RecipeCategory.MISC, result, experience, time).unlockedBy("has_ore", has(ore)).save(consumer, makeRL(name + "_from_smelting_" + path));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ore), RecipeCategory.MISC, result, experience, time / 2).unlockedBy("has_ore", has(ore)).save(consumer, makeRL(name + "_from_blasting_" + path));
	}

	protected void addCookingRecipes(Consumer<FinishedRecipe> consumer, ItemLike inputItem, Item result, float experience, int time)
	{
		String name = RegistryHelper.get(result).getPath();

		SimpleCookingRecipeBuilder.smelting(
						Ingredient.of(inputItem),
						RecipeCategory.FOOD,
						result,
						experience,
						time)
				.unlockedBy("has_item", has(inputItem))
				.save(consumer, new ResourceLocation(modid, name + "_from_smelting"));

		SimpleCookingRecipeBuilder.smoking(
						Ingredient.of(inputItem),
						RecipeCategory.FOOD,
						result,
						experience,
						time / 2)
				.unlockedBy("has_item", has(inputItem))
				.save(consumer, new ResourceLocation(modid, name + "_from_smoking"));

		SimpleCookingRecipeBuilder.campfireCooking(
						Ingredient.of(inputItem),
						RecipeCategory.FOOD,
						result,
						experience,
						time)
				.unlockedBy("has_item", has(inputItem))
				.save(consumer, new ResourceLocation(modid, name + "_from_campfire"));
	}

	protected void addPickaxeRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.TOOLS, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("XXX")
				.pattern(" Y ")
				.pattern(" Y ")
				.group("pickaxe")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}

	protected void addShovelRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.TOOLS, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("X")
				.pattern("Y")
				.pattern("Y")
				.group("shovel")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}

	protected void addAxeRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.TOOLS, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("XX")
				.pattern("XY")
				.pattern(" Y")
				.group("axe")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}

	protected void addSwordRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.COMBAT, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("X")
				.pattern("X")
				.pattern("Y")
				.group("sword")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}

	protected void addHoeRecipe(Consumer<FinishedRecipe> consumer, ItemRegistryObject<Item> outputItem, TagKey<Item> inputMaterial)
	{
		ShapedRecipeBuilder
				.shaped(RecipeCategory.TOOLS, outputItem)
				.define('X', inputMaterial)
				.define('Y', Tags.Items.RODS_WOODEN)
				.pattern("XX")
				.pattern(" Y")
				.pattern(" Y")
				.group("hoe")
				.unlockedBy("has_material", has(inputMaterial))
				.save(consumer);
	}


	protected void addArmorRecipes(Consumer<FinishedRecipe> consumer, TagKey<Item> inputMaterial, @Nullable ItemLike head, @Nullable ItemLike chest, @Nullable ItemLike legs, @Nullable ItemLike feet)
	{
		if (head != null)
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.COMBAT, head)
					.define('X', inputMaterial)
					.pattern("XXX")
					.pattern("X X")
					.group("helmet")
					.unlockedBy("has_material", has(inputMaterial))
					.save(consumer);
		}
		if (chest != null)
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.COMBAT, chest)
					.define('X', inputMaterial)
					.pattern("X X")
					.pattern("XXX")
					.pattern("XXX")
					.group("chestplate")
					.unlockedBy("has_material", has(inputMaterial))
					.save(consumer);
		}
		if (legs != null)
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.COMBAT, legs)
					.define('X', inputMaterial)
					.pattern("XXX")
					.pattern("X X")
					.pattern("X X")
					.group("leggings")
					.unlockedBy("has_material", has(inputMaterial))
					.save(consumer);
		}
		if (feet != null)
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.COMBAT, feet)
					.define('X', inputMaterial)
					.pattern("X X")
					.pattern("X X")
					.group("boots")
					.unlockedBy("has_material", has(inputMaterial))
					.save(consumer);
		}
	}

	protected void decompressRecipe(Consumer<FinishedRecipe> consumer, ItemLike output, ItemLike input, String name)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 9)
				.unlockedBy("has_item", has(output))
				.requires(input)
				.save(consumer, new ResourceLocation(modid, "conversions/" + name));
	}

	protected void decompressRecipe(Consumer<FinishedRecipe> consumer, ItemLike output, TagKey<Item> input, String name)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, 9)
				.unlockedBy("has_item", has(output))
				.requires(input)
				.save(consumer, new ResourceLocation(modid, "conversions/" + name));
	}

	protected ShapedRecipeBuilder compressRecipe(ItemLike output, TagKey<Item> input)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, output)
				.define('I', input)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", has(input));
	}

	protected ShapedRecipeBuilder compressRecipe(ItemLike output, TagKey<Item> input, ItemLike center)
	{
		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, output)
				.define('I', input)
				.define('J', center)
				.pattern("III")
				.pattern("IJI")
				.pattern("III")
				.unlockedBy("has_item", has(input));
	}

	protected ShapedRecipeBuilder godMetalCompressRecipe(ItemLike output, ItemLike input)
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("nuggetSize", 16);
		PartialNBTIngredient ingredient = PartialNBTIngredient.of(input, tag);

		return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, output)
				.define('I', ingredient)
				.pattern("III")
				.pattern("III")
				.pattern("III")
				.unlockedBy("has_item", has(input));
	}
}