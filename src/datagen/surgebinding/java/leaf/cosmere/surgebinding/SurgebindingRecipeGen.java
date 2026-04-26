/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class SurgebindingRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public SurgebindingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, Surgebinding.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Surgebinding.rl(path);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{
		for (Roshar.Gemstone gemstone : EnumUtils.GEMSTONE_TYPES)
		{
			compressRecipe(SurgebindingBlocks.GEM_BLOCKS.get(gemstone).getBlock(), CosmereTags.Items.GEM_TAGS.get(gemstone), SurgebindingItems.GEMSTONE_BROAMS.get(gemstone)).save(output);
			decompressRecipe(output, SurgebindingItems.GEMSTONE_BROAMS.get(gemstone).get(), SurgebindingBlocks.GEM_BLOCKS.get(gemstone), gemstone.getName() + "_block_deconstruct");

			//ores no longer obtained from blocks?
			//addOreSmeltingRecipes(output, SurgebindingBlocks.GEM_ORE.get(gemstone).getBlock(), SurgebindingItems.GEMSTONE_MARKS.get(gemstone).get(), 1.0f, 1000);
			//addOreSmeltingRecipes(output, SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(gemstone).getBlock(), SurgebindingItems.GEMSTONE_BROAMS.get(gemstone).get(), 1.0f, 1000);
		}
	}
}
