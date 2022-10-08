/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.common.registry.ItemsRegistry;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class HemalurgyRecipeGen extends RecipeProvider implements IConditionBuilder
{
	public HemalurgyRecipeGen(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		addOreSmeltingRecipes(consumer, HemalurgyItems.METAL_SPIKE.get(Metals.MetalType.IRON), ItemsRegistry.GUIDE.get(), 1.0f, 200);

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			addSpikes(consumer, metalType);
		}
	}

	protected static void addSpikes(Consumer<FinishedRecipe> consumer, Metals.MetalType metalType)
	{
		TagKey<Item> inputMaterial = metalType.getMetalIngotTag();

		if (metalType.hasHemalurgicEffect())
		{
			ShapedRecipeBuilder.shaped(HemalurgyItems.METAL_SPIKE.get(metalType)).define('X', inputMaterial).pattern("X").pattern("X").group("spike").unlockedBy("has_material", has(inputMaterial)).save(consumer);
		}
	}

	protected static void addOreSmeltingRecipes(Consumer<FinishedRecipe> consumer, ItemLike ore, Item result, float experience, int time)
	{
		String name = ResourceLocationHelper.get(result).getPath();
		String path = ResourceLocationHelper.get(ore.asItem()).getPath();
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ore), result, experience, time).unlockedBy("has_ore", has(ore)).save(consumer, Hemalurgy.rl(name + "_from_smelting_" + path));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ore), result, experience, time / 2).unlockedBy("has_ore", has(ore)).save(consumer, Hemalurgy.rl(name + "_from_blasting_" + path));
	}

}
