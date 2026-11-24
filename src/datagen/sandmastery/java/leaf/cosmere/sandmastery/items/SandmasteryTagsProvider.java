/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.items;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.diem.trial.OutsideHelp;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import leaf.cosmere.tag.BaseTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class SandmasteryTagsProvider extends BaseTagProvider
{
	public SandmasteryTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, Sandmastery.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags(HolderLookup.Provider registries)
	{
		getItemBuilder(CosmereTags.Items.CURIO_BELT).add(SandmasteryItems.SAND_POUCH_ITEM.asItem());
		getItemBuilder(OutsideHelp.ALLOWED_ITEMS).add(SandmasteryItems.SAND_POUCH_ITEM.asItem(), SandmasteryItems.QIDO_ITEM.asItem());

		getBlockBuilder(BlockTags.MINEABLE_WITH_SHOVEL).add(SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SAND.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock(), SandmasteryBlocks.TALDAIN_GLOWING_SAND.getBlock());
		getBlockBuilder(BlockTags.SAND).add(SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SAND.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock(), SandmasteryBlocks.TALDAIN_GLOWING_SAND.getBlock());
		getBlockBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
			.add(SandmasteryBlocks.SAND_JAR_BLOCK.getBlock(), SandmasteryBlocks.WATER_DRIPPER.getBlock(),
				SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_CUT_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_CHISELED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_COBBLED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_BRICKED_SANDSTONE.getBlock(),
				SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_CUT_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_CHISELED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_COBBLED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_BRICKED_SANDSTONE.getBlock());
		getBlockBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(
				SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_STAIRS.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_STAIRS.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_WALL.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_WALL.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_CUT_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_CUT_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_STAIRS.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_STAIRS.getBlock());
		getBlockBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(
				SandmasteryBlocks.TALDAIN_BLACK_GILDED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_GILDED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_GILDED_CUT_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_GILDED_CUT_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_GILDED_SMOOTH_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_GILDED_SMOOTH_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_GILDED_CHISELED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_GILDED_CHISELED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_GILDED_COBBLED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_GILDED_COBBLED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_GILDED_BRICKED_SANDSTONE.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_GILDED_BRICKED_SANDSTONE.getBlock());
		getBlockBuilder(BlockTags.SLABS).add(SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_CUT_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_CUT_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_SLAB.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_SLAB.getBlock());
		getBlockBuilder(BlockTags.STAIRS).add(SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_STAIRS.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_STAIRS.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_STAIRS.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_STAIRS.getBlock());
		getBlockBuilder(BlockTags.WALLS).add(SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_WALL.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_WALL.getBlock());
		getItemBuilder(ItemTags.SLABS).add(SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_BLACK_CUT_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_WHITE_CUT_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_SLAB.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_SLAB.asItem());
		getItemBuilder(ItemTags.STAIRS).add(SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_STAIRS.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_STAIRS.asItem(), SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_STAIRS.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_STAIRS.asItem());
		getItemBuilder(ItemTags.WALLS).add(SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_WALL.asItem(), SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_WALL.asItem());
		getBlockBuilder(BlockTags.MINEABLE_WITH_AXE).add(SandmasteryBlocks.SAND_SPREADING_TUB_BLOCK.getBlock());
	}

}