/*
 * File updated ~ 26 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.CosmereTags;
import net.minecraft.core.Holder;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class RecipeReloadListener extends SimplePreparableReloadListener<Void>
{
	private final ReloadableServerResources resources;

	public RecipeReloadListener(final ReloadableServerResources managerSupplier)
	{
		this.resources = managerSupplier;
	}

	@Override
	protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler)
	{
		return null;
	}

	@Override
	protected void apply(Void object, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		final ReloadableServerResources serverResources = this.resources;
		final TagKey<Item> containsMetal = CosmereTags.Items.CONTAINS_METAL;
		final RecipeManager recipeManager = serverResources.getRecipeManager();
		final Collection<Recipe<?>> recipes = recipeManager.getRecipes();

		for (var recipe : recipes)
		{
			final ItemStack resultItem = recipe.getResultItem();

			if (resultItem.is(containsMetal))
			{
				continue;
			}

			CheckRecipeForMetal(containsMetal, recipe, resultItem);
		}
	}


	public static void CheckRecipeForMetal(TagKey<Item> containsMetal, Recipe<?> recipe, ItemStack resultItem)
	{
		for (Ingredient ingredient : recipe.getIngredients())
		{
			for (ItemStack itemStack : ingredient.getItems())
			{
				if (itemStack.is(containsMetal))
				{
					//found one
					final Holder.Reference<Item> itemReference = resultItem.getItem().builtInRegistryHolder();
					List<TagKey<Item>> allTags = itemReference.tags().collect(Collectors.toList());
					allTags.add(CosmereTags.Items.CONTAINS_METAL);
					itemReference.bindTags(allTags);

					CosmereAPI.logger.info(itemReference + " has been identified as containing metal.");
					return;
				}
			}
		}
	}
}