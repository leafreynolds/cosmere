/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.registries.AllomancyRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class AllomancyRecipeGen extends RecipeProvider implements IConditionBuilder
{
	public AllomancyRecipeGen(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shaped(AllomancyItems.METAL_VIAL.get()).define('X', Items.IRON_NUGGET).define('Y', Items.GLASS).pattern("X").pattern("Y").unlockedBy("has_material", has(Items.GLASS)).save(consumer);

		ShapelessRecipeBuilder.shapeless(AllomancyItems.COIN_POUCH.get(), 1)
				.unlockedBy("has_item", has(Tags.Items.LEATHER))
				.requires(Tags.Items.LEATHER)
				.requires(Tags.Items.STRING)
				.save(consumer);

		SpecialRecipeBuilder.special((SimpleRecipeSerializer<?>) AllomancyRecipes.VIAL_RECIPE_SERIALIZER.get()).save(consumer, Allomancy.rl("vial_mixing").toString());

	}
}
