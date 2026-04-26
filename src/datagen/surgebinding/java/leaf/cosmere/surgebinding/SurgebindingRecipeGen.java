
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
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class SurgebindingRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public SurgebindingRecipeGen(PackOutput output, ExistingFileHelper existingFileHelper)
	{
		super(output, existingFileHelper, Surgebinding.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Surgebinding.rl(path);
	}

	@Override
	protected void addRecipes(Consumer<FinishedRecipe> consumer)
	{
		for (Roshar.Gemstone gemstone : EnumUtils.GEMSTONE_TYPES)
		{
			compressRecipe(SurgebindingBlocks.GEM_BLOCKS.get(gemstone).getBlock(), CosmereTags.Items.GEM_TAGS.get(gemstone), SurgebindingItems.GEMSTONE_BROAMS.get(gemstone)).save(consumer);
			decompressRecipe(consumer, SurgebindingItems.GEMSTONE_BROAMS.get(gemstone).get(), SurgebindingBlocks.GEM_BLOCKS.get(gemstone), gemstone.getName() + "_block_deconstruct");

			//ores no longer obtained from blocks?
			//addOreSmeltingRecipes(consumer, SurgebindingBlocks.GEM_ORE.get(gemstone).getBlock(), SurgebindingItems.GEMSTONE_MARKS.get(gemstone).get(), 1.0f, 1000);
			//addOreSmeltingRecipes(consumer, SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(gemstone).getBlock(), SurgebindingItems.GEMSTONE_BROAMS.get(gemstone).get(), 1.0f, 1000);
		}
	}
}
