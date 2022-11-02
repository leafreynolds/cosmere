/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.cosmere.sandmastery.common.registries;

import leaf.cosmere.common.blocks.BaseBlock;
import leaf.cosmere.common.registration.impl.BlockDeferredRegister;
import leaf.cosmere.common.registration.impl.BlockRegistryObject;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.SandLayerBlock;
import net.minecraft.world.item.BlockItem;

public class SandmasteryBlocksRegistry
{
	public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Sandmastery.MODID);
	public static final BlockRegistryObject<SandLayerBlock, BlockItem> WHITE_SAND_LAYER = BLOCKS.register("white_sand", SandLayerBlock::new);
	public static final BlockRegistryObject<SandLayerBlock, BlockItem> BLACK_SAND_LAYER = BLOCKS.register("black_sand", SandLayerBlock::new);

}
