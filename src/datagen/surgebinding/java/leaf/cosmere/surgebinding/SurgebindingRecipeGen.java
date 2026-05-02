
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
import leaf.cosmere.surgebinding.common.registries.SurgebindingRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		//Gemstone Blocks
		Roshar.Gemstone[] gemstonesList = {Roshar.Gemstone.SAPPHIRE, Roshar.Gemstone.SMOKESTONE, Roshar.Gemstone.RUBY, Roshar.Gemstone.DIAMOND, Roshar.Gemstone.GARNET, Roshar.Gemstone.ZIRCON, Roshar.Gemstone.TOPAZ, Roshar.Gemstone.HELIODOR};
		for (Roshar.Gemstone gemstone : gemstonesList)
		{
			compressRecipe(SurgebindingBlocks.GEM_BLOCKS.get(gemstone).getBlock(), CosmereTags.Items.GEM_TAGS.get(gemstone)).save(consumer);
			decompressRecipe(consumer, SurgebindingItems.GEMSTONE.get(gemstone), SurgebindingBlocks.GEM_BLOCKS.get(gemstone),gemstone.getName()+"_block_deconstruct");
		}
		compressRecipe(SurgebindingBlocks.GEM_BLOCKS.get(Roshar.Gemstone.AMETHYST).getBlock(),CosmereTags.Items.GEM_TAGS.get(Roshar.Gemstone.AMETHYST)).save(consumer);
		decompressRecipe(consumer, Items.AMETHYST_SHARD, SurgebindingBlocks.GEM_BLOCKS.get(Roshar.Gemstone.AMETHYST), "amethyst_block_deconstruct");

		//Cutting Gemstones into Cut Gemstones
		for(Roshar.Gemstone gemstone : gemstonesList)
		{
			RecipeProvider.stonecutterResultFromBase(consumer,RecipeCategory.MISC,SurgebindingItems.GEMSTONE_SMALL.get(gemstone),SurgebindingItems.GEMSTONE.get(gemstone),16);
			RecipeProvider.stonecutterResultFromBase(consumer,RecipeCategory.MISC,SurgebindingItems.GEMSTONE_MEDIUM.get(gemstone),SurgebindingItems.GEMSTONE.get(gemstone),4);
			RecipeProvider.stonecutterResultFromBase(consumer,RecipeCategory.MISC,SurgebindingItems.GEMSTONE_LARGE.get(gemstone),SurgebindingItems.GEMSTONE.get(gemstone),1);
		}

		SpecialRecipeBuilder
				.special(SurgebindingRecipes.PLATE_CHARGE.get())
				.save(consumer, Surgebinding.rl("plate_charging").toString());

		RecipeProvider.stonecutterResultFromBase(consumer,RecipeCategory.MISC,SurgebindingItems.GEMSTONE_SMALL.get(Roshar.Gemstone.EMERALD),Items.EMERALD,16);
		RecipeProvider.stonecutterResultFromBase(consumer,RecipeCategory.MISC,SurgebindingItems.GEMSTONE_MEDIUM.get(Roshar.Gemstone.EMERALD),Items.EMERALD,4);
		RecipeProvider.stonecutterResultFromBase(consumer,RecipeCategory.MISC,SurgebindingItems.GEMSTONE_LARGE.get(Roshar.Gemstone.EMERALD),Items.EMERALD,1);
		RecipeProvider.stonecutterResultFromBase(consumer,RecipeCategory.MISC,SurgebindingItems.GEMSTONE_SMALL.get(Roshar.Gemstone.AMETHYST),Items.AMETHYST_SHARD,16);
		RecipeProvider.stonecutterResultFromBase(consumer,RecipeCategory.MISC,SurgebindingItems.GEMSTONE_MEDIUM.get(Roshar.Gemstone.AMETHYST),Items.AMETHYST_SHARD,4);
		RecipeProvider.stonecutterResultFromBase(consumer,RecipeCategory.MISC,SurgebindingItems.GEMSTONE_LARGE.get(Roshar.Gemstone.AMETHYST),Items.AMETHYST_SHARD,1);

		//Foods and stuff
		RecipeProvider.smeltingResultFromBase(consumer, SurgebindingItems.COOKED_CHULL_LEG, SurgebindingItems.RAW_CHULL_LEG);
		RecipeProvider.smeltingResultFromBase(consumer, SurgebindingItems.COOKED_CHULL_MEAT, SurgebindingItems.RAW_CHULL_MEAT);

	}
}
