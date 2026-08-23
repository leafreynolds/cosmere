
/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.recipes.ShardplateChargingRecipe;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
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
		//Gemstone Blocks
		Roshar.Gemstone[] gemstonesList = {Roshar.Gemstone.SAPPHIRE, Roshar.Gemstone.SMOKESTONE, Roshar.Gemstone.RUBY, Roshar.Gemstone.DIAMOND, Roshar.Gemstone.GARNET, Roshar.Gemstone.ZIRCON, Roshar.Gemstone.TOPAZ, Roshar.Gemstone.HELIODOR};
		for (Roshar.Gemstone gemstone : gemstonesList)
		{
			compressRecipe(SurgebindingBlocks.GEM_BLOCKS.get(gemstone).getBlock(), CosmereTags.Items.GEM_TAGS.get(gemstone)).save(output);
			decompressRecipe(output, SurgebindingItems.GEMSTONE.get(gemstone), SurgebindingBlocks.GEM_BLOCKS.get(gemstone), gemstone.getName() + "_block_deconstruct");
		}
		compressRecipe(SurgebindingBlocks.GEM_BLOCKS.get(Roshar.Gemstone.AMETHYST).getBlock(), CosmereTags.Items.GEM_TAGS.get(Roshar.Gemstone.AMETHYST)).save(output);
		decompressRecipe(output, Items.AMETHYST_SHARD, SurgebindingBlocks.GEM_BLOCKS.get(Roshar.Gemstone.AMETHYST), "amethyst_block_deconstruct");

		//Cutting Gemstones into Cut Gemstones
		for (Roshar.Gemstone gemstone : gemstonesList)
		{
			RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.MISC, SurgebindingItems.GEMSTONE_SMALL.get(gemstone), SurgebindingItems.GEMSTONE.get(gemstone), 16);
			RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.MISC, SurgebindingItems.GEMSTONE_MEDIUM.get(gemstone), SurgebindingItems.GEMSTONE.get(gemstone), 4);
			RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.MISC, SurgebindingItems.GEMSTONE_LARGE.get(gemstone), SurgebindingItems.GEMSTONE.get(gemstone), 1);
		}

		SpecialRecipeBuilder
				.special(ShardplateChargingRecipe::new)
				.save(output, Surgebinding.rl("plate_charging").toString());

		RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.MISC, SurgebindingItems.GEMSTONE_SMALL.get(Roshar.Gemstone.EMERALD), Items.EMERALD, 16);
		RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.MISC, SurgebindingItems.GEMSTONE_MEDIUM.get(Roshar.Gemstone.EMERALD), Items.EMERALD, 4);
		RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.MISC, SurgebindingItems.GEMSTONE_LARGE.get(Roshar.Gemstone.EMERALD), Items.EMERALD, 1);
		RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.MISC, SurgebindingItems.GEMSTONE_SMALL.get(Roshar.Gemstone.AMETHYST), Items.AMETHYST_SHARD, 16);
		RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.MISC, SurgebindingItems.GEMSTONE_MEDIUM.get(Roshar.Gemstone.AMETHYST), Items.AMETHYST_SHARD, 4);
		RecipeProvider.stonecutterResultFromBase(output, RecipeCategory.MISC, SurgebindingItems.GEMSTONE_LARGE.get(Roshar.Gemstone.AMETHYST), Items.AMETHYST_SHARD, 1);

		//Foods and stuff
		RecipeProvider.smeltingResultFromBase(output, SurgebindingItems.COOKED_CHULL_LEG, SurgebindingItems.RAW_CHULL_LEG);
		RecipeProvider.smeltingResultFromBase(output, SurgebindingItems.COOKED_CHULL_MEAT, SurgebindingItems.RAW_CHULL_MEAT);

	}
}
