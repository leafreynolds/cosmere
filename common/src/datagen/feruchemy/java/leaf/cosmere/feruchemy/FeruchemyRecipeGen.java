/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class FeruchemyRecipeGen extends RecipeProvider implements IConditionBuilder
{
	public FeruchemyRecipeGen(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			addBasicMetalmindRecipes(consumer, metalType);
		}
	}

	protected static void addBasicMetalmindRecipes(Consumer<FinishedRecipe> consumer, Metals.MetalType metalType)
	{
		TagKey<Item> inputMaterial = metalType.getMetalIngotTag();

		if (metalType.hasFeruchemicalEffect())
		{
			ShapedRecipeBuilder.shaped(FeruchemyItems.METAL_NECKLACES.get(metalType)).define('X', inputMaterial).pattern("XXX").pattern("X X").pattern(" X ").group("necklace").unlockedBy("has_material", has(inputMaterial)).save(consumer);
			ShapedRecipeBuilder.shaped(FeruchemyItems.METAL_RINGS.get(metalType)).define('X', inputMaterial).pattern(" X ").pattern("X X").pattern(" X ").group("ring").unlockedBy("has_material", has(inputMaterial)).save(consumer);
			ShapedRecipeBuilder.shaped(FeruchemyItems.METAL_BRACELETS.get(metalType)).define('X', inputMaterial).pattern(" X ").pattern("X X").pattern("X X").group("bracelet").unlockedBy("has_material", has(inputMaterial)).save(consumer);
		}
	}

}
