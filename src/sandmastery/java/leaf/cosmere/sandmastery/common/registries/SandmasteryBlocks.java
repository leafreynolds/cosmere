/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.common.registration.impl.BlockDeferredRegister;
import leaf.cosmere.common.registration.impl.BlockRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.*;
import net.minecraft.world.item.BlockItem;

public class SandmasteryBlocks
{
	public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Sandmastery.MODID);
	public static final BlockRegistryObject<TaldainBlackSandLayerBlock, BlockItem> TALDAIN_BLACK_SAND_LAYER = BLOCKS.register("taldain_sand_layer", TaldainBlackSandLayerBlock::new);
	public static final BlockRegistryObject<TaldainWhiteSandLayerBlock, BlockItem> TALDAIN_WHITE_SAND_LAYER = BLOCKS.register("charged_taldain_sand_layer", TaldainWhiteSandLayerBlock::new);
	public static final BlockRegistryObject<TaldainBlackSandBlock, BlockItem> TALDAIN_BLACK_SAND = BLOCKS.register("taldain_sand", TaldainBlackSandBlock::new);
	public static final BlockRegistryObject<TaldainWhiteSandBlock, BlockItem> TALDAIN_WHITE_SAND = BLOCKS.register("charged_taldain_sand", TaldainWhiteSandBlock::new);
	public static final BlockRegistryObject<TaldainGlowingSandBlock, BlockItem> TALDAIN_GLOWING_SAND = BLOCKS.register("glowing_taldain_sand", TaldainGlowingSandBlock::new);
	public static final BlockRegistryObject<TaldainBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_SANDSTONE = BLOCKS.register("taldain_sandstone", () -> new TaldainBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_SANDSTONE = BLOCKS.register("charged_taldain_sandstone", () -> new TaldainWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_CUT_SANDSTONE = BLOCKS.register("cut_taldain_sandstone", () -> new TaldainBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_CUT_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_CUT_SANDSTONE = BLOCKS.register("charged_cut_taldain_sandstone", () -> new TaldainWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_CUT_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_SMOOTH_SANDSTONE = BLOCKS.register("smooth_taldain_sandstone", () -> new TaldainBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_SMOOTH_SANDSTONE = BLOCKS.register("charged_smooth_taldain_sandstone", () -> new TaldainWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_CHISELED_SANDSTONE = BLOCKS.register("chiseled_taldain_sandstone", () -> new TaldainBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_CHISELED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_CHISELED_SANDSTONE = BLOCKS.register("charged_chiseled_taldain_sandstone", () -> new TaldainWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_CHISELED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_COBBLED_SANDSTONE = BLOCKS.register("cobbled_taldain_sandstone", () -> new TaldainBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_COBBLED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_COBBLED_SANDSTONE = BLOCKS.register("charged_cobbled_taldain_sandstone", () -> new TaldainWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_COBBLED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_BRICKED_SANDSTONE = BLOCKS.register("bricked_taldain_sandstone", () -> new TaldainBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_BRICKED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_BRICKED_SANDSTONE = BLOCKS.register("charged_bricked_taldain_sandstone", () -> new TaldainWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_BRICKED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneSlabBlock, BlockItem> TALDAIN_BLACK_SANDSTONE_SLAB = BLOCKS.register("taldain_sandstone_slab", () -> new TaldainSandstoneSlabBlock(false, () -> SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_SLAB.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneSlabBlock, BlockItem> TALDAIN_WHITE_SANDSTONE_SLAB = BLOCKS.register("charged_taldain_sandstone_slab", () -> new TaldainSandstoneSlabBlock(true, () -> SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_SLAB.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneStairBlock, BlockItem> TALDAIN_BLACK_SANDSTONE_STAIRS = BLOCKS.register("taldain_sandstone_stairs", () -> new TaldainSandstoneStairBlock(false, () -> SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE.getBlock(), () -> SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_STAIRS.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneStairBlock, BlockItem> TALDAIN_WHITE_SANDSTONE_STAIRS = BLOCKS.register("charged_taldain_sandstone_stairs", () -> new TaldainSandstoneStairBlock(true, () -> SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE.getBlock(), () -> SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_STAIRS.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneWallBlock, BlockItem> TALDAIN_BLACK_SANDSTONE_WALL = BLOCKS.register("taldain_sandstone_wall", () -> new TaldainSandstoneWallBlock(false, () -> SandmasteryBlocks.TALDAIN_WHITE_SANDSTONE_WALL.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneWallBlock, BlockItem> TALDAIN_WHITE_SANDSTONE_WALL = BLOCKS.register("charged_taldain_sandstone_wall", () -> new TaldainSandstoneWallBlock(true, () -> SandmasteryBlocks.TALDAIN_BLACK_SANDSTONE_WALL.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneSlabBlock, BlockItem> TALDAIN_BLACK_CUT_SANDSTONE_SLAB = BLOCKS.register("cut_taldain_sandstone_slab", () -> new TaldainSandstoneSlabBlock(false, () -> SandmasteryBlocks.TALDAIN_WHITE_CUT_SANDSTONE_SLAB.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneSlabBlock, BlockItem> TALDAIN_WHITE_CUT_SANDSTONE_SLAB = BLOCKS.register("charged_cut_taldain_sandstone_slab", () -> new TaldainSandstoneSlabBlock(true, () -> SandmasteryBlocks.TALDAIN_BLACK_CUT_SANDSTONE_SLAB.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneSlabBlock, BlockItem> TALDAIN_BLACK_SMOOTH_SANDSTONE_SLAB = BLOCKS.register("smooth_taldain_sandstone_slab", () -> new TaldainSandstoneSlabBlock(false, () -> SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_SLAB.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneSlabBlock, BlockItem> TALDAIN_WHITE_SMOOTH_SANDSTONE_SLAB = BLOCKS.register("charged_smooth_taldain_sandstone_slab", () -> new TaldainSandstoneSlabBlock(true, () -> SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_SLAB.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneStairBlock, BlockItem> TALDAIN_BLACK_SMOOTH_SANDSTONE_STAIRS = BLOCKS.register("smooth_taldain_sandstone_stairs", () -> new TaldainSandstoneStairBlock(false, () -> SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE.getBlock(), () -> SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE_STAIRS.getBlock()));
	public static final BlockRegistryObject<TaldainSandstoneStairBlock, BlockItem> TALDAIN_WHITE_SMOOTH_SANDSTONE_STAIRS = BLOCKS.register("charged_smooth_taldain_sandstone_stairs", () -> new TaldainSandstoneStairBlock(true, () -> SandmasteryBlocks.TALDAIN_WHITE_SMOOTH_SANDSTONE.getBlock(), () -> SandmasteryBlocks.TALDAIN_BLACK_SMOOTH_SANDSTONE_STAIRS.getBlock()));
	public static final BlockRegistryObject<TaldainGildedBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_GILDED_SANDSTONE = BLOCKS.register("gilded_taldain_sandstone", () -> new TaldainGildedBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_GILDED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainGildedWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_GILDED_SANDSTONE = BLOCKS.register("charged_gilded_taldain_sandstone", () -> new TaldainGildedWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_GILDED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainGildedBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_GILDED_CUT_SANDSTONE = BLOCKS.register("gilded_cut_taldain_sandstone", () -> new TaldainGildedBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_GILDED_CUT_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainGildedWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_GILDED_CUT_SANDSTONE = BLOCKS.register("charged_gilded_cut_taldain_sandstone", () -> new TaldainGildedWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_GILDED_CUT_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainGildedBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_GILDED_SMOOTH_SANDSTONE = BLOCKS.register("gilded_smooth_taldain_sandstone", () -> new TaldainGildedBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_GILDED_SMOOTH_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainGildedWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_GILDED_SMOOTH_SANDSTONE = BLOCKS.register("charged_gilded_smooth_taldain_sandstone", () -> new TaldainGildedWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_GILDED_SMOOTH_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainGildedBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_GILDED_CHISELED_SANDSTONE = BLOCKS.register("gilded_chiseled_taldain_sandstone", () -> new TaldainGildedBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_GILDED_CHISELED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainGildedWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_GILDED_CHISELED_SANDSTONE = BLOCKS.register("charged_gilded_chiseled_taldain_sandstone", () -> new TaldainGildedWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_GILDED_CHISELED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_GILDED_COBBLED_SANDSTONE = BLOCKS.register("gilded_cobbled_taldain_sandstone", () -> new TaldainBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_GILDED_COBBLED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_GILDED_COBBLED_SANDSTONE = BLOCKS.register("charged_gilded_cobbled_taldain_sandstone", () -> new TaldainWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_GILDED_COBBLED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainBlackSandstoneBlock, BlockItem> TALDAIN_BLACK_GILDED_BRICKED_SANDSTONE = BLOCKS.register("gilded_bricked_taldain_sandstone", () -> new TaldainBlackSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_WHITE_GILDED_BRICKED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<TaldainWhiteSandstoneBlock, BlockItem> TALDAIN_WHITE_GILDED_BRICKED_SANDSTONE = BLOCKS.register("charged_gilded_bricked_taldain_sandstone", () -> new TaldainWhiteSandstoneBlock(() -> SandmasteryBlocks.TALDAIN_BLACK_GILDED_BRICKED_SANDSTONE.getBlock()));
	public static final BlockRegistryObject<WaterDripperBlock, BlockItem> WATER_DRIPPER = BLOCKS.register("water_dripper", WaterDripperBlock::new);
	public static final BlockRegistryObject<SandJarBlock, BlockItem> SAND_JAR_BLOCK = BLOCKS.register("sand_jar_block", SandJarBlock::new);
	public static final BlockRegistryObject<TemporarySandBlock, BlockItem> TEMPORARY_SAND_BLOCK = BLOCKS.register("temporary_sand_block", TemporarySandBlock::new);
	public static final BlockRegistryObject<SandSpreadingTubBlock, BlockItem> SAND_SPREADING_TUB_BLOCK = BLOCKS.register("sand_spreading_tub", SandSpreadingTubBlock::new);
	public static final BlockRegistryObject<DiemStoneBlock, BlockItem> DIEM_STONE = BLOCKS.register("diem_stone", DiemStoneBlock::new);
	public static final BlockRegistryObject<DiemCheckpointBlock, BlockItem> DIEM_CHECKPOINT = BLOCKS.register("diem_checkpoint", DiemCheckpointBlock::new);

}
