/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.ItemsRegistry;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
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

public class HemalurgyRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public HemalurgyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, Hemalurgy.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Hemalurgy.rl(path);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{
		addOreSmeltingRecipes(output, HemalurgyItems.METAL_SPIKE.get(Metals.MetalType.IRON), ItemsRegistry.GUIDE.get(), 1.0f, 200);

		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			addSpikes(output, metalType);
		}
	}

	protected static void addSpikes(RecipeOutput output, Metals.MetalType metalType)
	{
		TagKey<Item> inputMaterial = metalType.getMetalIngotTag();

		if (metalType.hasHemalurgicEffect())
		{
			ShapedRecipeBuilder
					.shaped(RecipeCategory.TOOLS, HemalurgyItems.METAL_SPIKE.get(metalType))
					.define('X', inputMaterial)
					.pattern("X")
					.pattern("X")
					.group("spike")
					.unlockedBy("has_material", has(inputMaterial)).save(output);
		}
	}

}
