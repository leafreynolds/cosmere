/*
 * File updated ~ 4 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.sandmastery;

import leaf.cosmere.BaseRecipeProvider;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class SandmasteryRecipeGen extends BaseRecipeProvider implements IConditionBuilder
{
	public SandmasteryRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, Sandmastery.MODID);
	}

	@Override
	protected ResourceLocation makeRL(String path)
	{
		return Sandmastery.rl(path);
	}

	@Override
	protected void addRecipes(RecipeOutput output)
	{
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SandmasteryItems.QIDO_ITEM.get())
				.define('H', Items.GOAT_HORN)
				.define('S', Tags.Items.STRINGS)
				.pattern("SSS")
				.pattern("S S")
				.pattern(" H ")
				.unlockedBy("has_material", has(Items.GOAT_HORN))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SandmasteryItems.JAR_ITEM.get())
				.define('G', Tags.Items.GLASS_BLOCKS)
				.pattern("G G")
				.pattern("G G")
				.pattern(" G ")
				.unlockedBy("has_material", has(Tags.Items.GLASS_BLOCKS))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, SandmasteryItems.SAND_POUCH_ITEM.get())
				.define('L', Tags.Items.LEATHERS)
				.define('B', Items.LEAD)
				.define('J', SandmasteryItems.SAND_JAR_ITEM)
				.pattern("BLL")
				.pattern("LJL")
				.pattern("LLL")
				.unlockedBy("has_material", has(SandmasteryItems.SAND_JAR_ITEM))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SandmasteryBlocks.SAND_SPREADING_TUB_BLOCK.getBlock())
				.define('W', ItemTags.WOODEN_SLABS)
				.pattern("W W")
				.pattern("WWW")
				.unlockedBy("has_material", has(ItemTags.WOODEN_SLABS))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, SandmasteryItems.SAND_JAR_ITEM.get())
				.requires(SandmasteryBlocks.SAND_JAR_BLOCK.asItem())
				.unlockedBy("has_material", has(SandmasteryItems.SAND_JAR_ITEM))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, SandmasteryBlocks.SAND_JAR_BLOCK.asItem())
				.requires(SandmasteryItems.SAND_JAR_ITEM)
				.unlockedBy("has_material", has(SandmasteryBlocks.SAND_JAR_BLOCK.asItem()))
				.save(output);

		// same recipes as vanilla sandstone, once for black sand and once for charged (white) sand
		sandstoneRecipes(output, new SandstoneSet(SandmasteryBlocks.TALDAIN_BLACK_SAND.asItem(), SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_BLACK_CUT_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_BLACK_CHISELED_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_BLACK_COBBLED_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_BLACK_BRICKED_SANDSTONE.asItem(),
				SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_STAIRS.asItem(), SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_WALL.asItem(), SandmasteryBlocks.TALDAIN_BLACK_CUT_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_STAIRS.asItem()));
		sandstoneRecipes(output, new SandstoneSet(SandmasteryBlocks.TALDAIN_WHITE_SAND.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_WHITE_CUT_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_WHITE_CHISELED_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_WHITE_COBBLED_SANDSTONE.asItem(), SandmasteryBlocks.TALDAIN_WHITE_BRICKED_SANDSTONE.asItem(),
				SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_STAIRS.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_WALL.asItem(), SandmasteryBlocks.TALDAIN_WHITE_CUT_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_STAIRS.asItem()));
	}

	private record SandstoneSet(ItemLike sand, ItemLike sandstone, ItemLike cut, ItemLike smooth, ItemLike chiseled, ItemLike cobbled, ItemLike bricked,
	                            ItemLike slab, ItemLike stairs, ItemLike wall, ItemLike cutSlab, ItemLike smoothSlab, ItemLike smoothStairs)
	{
	}

	private void sandstoneRecipes(RecipeOutput output, SandstoneSet set)
	{
		final ItemLike sandstone = set.sandstone();
		final ItemLike cut = set.cut();
		final ItemLike smooth = set.smooth();
		final ItemLike chiseled = set.chiseled();

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, sandstone)
				.define('#', set.sand())
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_sand", has(set.sand()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, cut, 4)
				.define('#', sandstone)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_sandstone", has(sandstone))
				.save(output);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(sandstone), RecipeCategory.BUILDING_BLOCKS, smooth, 0.1F, 200)
				.unlockedBy("has_sandstone", has(sandstone))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, chiseled)
				.define('#', set.slab())
				.pattern("#")
				.pattern("#")
				.unlockedBy("has_sandstone", has(sandstone))
				.save(output);

		// slabs and stairs accept the same alternative inputs vanilla's do
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.slab(), 6)
				.define('#', Ingredient.of(sandstone, chiseled))
				.pattern("###")
				.unlockedBy("has_sandstone", has(sandstone))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.stairs(), 4)
				.define('#', Ingredient.of(sandstone, chiseled, cut))
				.pattern("#  ")
				.pattern("## ")
				.pattern("###")
				.unlockedBy("has_sandstone", has(sandstone))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, set.wall(), 6)
				.define('#', sandstone)
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_sandstone", has(sandstone))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.cutSlab(), 6)
				.define('#', cut)
				.pattern("###")
				.unlockedBy("has_cut_sandstone", has(cut))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.smoothSlab(), 6)
				.define('#', smooth)
				.pattern("###")
				.unlockedBy("has_smooth_sandstone", has(smooth))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.smoothStairs(), 4)
				.define('#', smooth)
				.pattern("#  ")
				.pattern("## ")
				.pattern("###")
				.unlockedBy("has_smooth_sandstone", has(smooth))
				.save(output);

		stonecutterRecipe(output, sandstone, cut);
		stonecutterRecipe(output, sandstone, chiseled);
		stonecutterRecipe(output, sandstone, set.cobbled());
		stonecutterRecipe(output, sandstone, set.bricked());
		stonecutterRecipe(output, sandstone, set.slab(), 2);
		stonecutterRecipe(output, sandstone, set.stairs());
		stonecutterRecipe(output, sandstone, set.wall());
		stonecutterRecipe(output, sandstone, set.cutSlab(), 2);
		stonecutterRecipe(output, cut, set.cutSlab(), 2);
		stonecutterRecipe(output, smooth, set.smoothSlab(), 2);
		stonecutterRecipe(output, smooth, set.smoothStairs());
	}
}
