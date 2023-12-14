/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.common.registries;

import leaf.cosmere.common.registration.impl.BlockDeferredRegister;
import leaf.cosmere.example.common.Example;

public class ExampleBlocks
{
	public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(Example.MODID);

	//public static final BlockRegistryObject<Block, BlockItem> EXAMPLE_BLOCK = BLOCKS.register("example", BaseBlock::new);


}
