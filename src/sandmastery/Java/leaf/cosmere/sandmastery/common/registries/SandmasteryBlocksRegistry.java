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
import net.minecraft.world.level.block.Block;

public class SandmasteryBlocksRegistry
{
	public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Sandmastery.MODID);
	public static final BlockRegistryObject<TaldainSandLayerBlock, BlockItem> TALDAIN_SAND_LAYER = BLOCKS.register("taldain_sand_layer", TaldainSandLayerBlock::new);
	public static final BlockRegistryObject<TaldainSandBlock, BlockItem> TALDAIN_SAND = BLOCKS.register("taldain_sand", TaldainSandBlock::new);
	public static final BlockRegistryObject<SandJarBlock, BlockItem> SAND_JAR_BLOCK = BLOCKS.register("sand_jar_block", SandJarBlock::new);
	public static final BlockRegistryObject<TemporarySandBlock, BlockItem> TEMPORARY_SAND_BLOCK = BLOCKS.register("temporary_sand_block", TemporarySandBlock::new);
	public static final BlockRegistryObject<SandSpreadingTubBlock, BlockItem> SAND_SPREADING_TUB_BLOCK = BLOCKS.register("sand_spreading_tub", SandSpreadingTubBlock::new);

}
