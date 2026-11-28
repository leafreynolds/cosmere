/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class FeruchemyRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public FeruchemyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, Feruchemy.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Feruchemy.rl(path);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			addBasicMetalmindRecipes(output, metalType);
		}
	}

	protected static void addBasicMetalmindRecipes(RecipeOutput output, Metals.MetalType metalType)
	{
		TagKey<Item> inputMaterial = metalType.getMetalIngotTag();

		if (metalType.hasFeruchemicalEffect())
		{
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FeruchemyItems.METAL_NECKLACES.get(metalType)).define('X', inputMaterial).pattern("XXX").pattern("X X").pattern(" X ").group("necklace").unlockedBy("has_material", has(inputMaterial)).save(output);
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FeruchemyItems.METAL_RINGS.get(metalType)).define('X', inputMaterial).pattern(" X ").pattern("X X").pattern(" X ").group("ring").unlockedBy("has_material", has(inputMaterial)).save(output);
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FeruchemyItems.METAL_BRACELETS.get(metalType)).define('X', inputMaterial).pattern(" X ").pattern("X X").pattern("X X").group("bracelet").unlockedBy("has_material", has(inputMaterial)).save(output);
		}
	}

}
