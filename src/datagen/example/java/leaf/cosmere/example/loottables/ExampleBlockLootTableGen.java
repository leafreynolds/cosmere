/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.example.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.example.common.registries.ExampleBlocks;
import leaf.cosmere.loottables.BaseBlockLootTables;
import net.minecraft.world.level.block.Block;

public class ExampleBlockLootTableGen extends BaseBlockLootTables
{
	@Override
	protected void generate()
	{
		//first catch any blocks that don't drop self, like ores
		for (IBlockProvider itemRegistryObject : ExampleBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			//if (block instanceof MetalOreBlock oreBlock)
			//{
			//      this.add(oreBlock, (ore) -> createOreDrop(ore, ItemsRegistry.METAL_RAW_ORE.get(oreBlock.getMetalType())));
			//}
		}

		//then make the rest drop themselves.
		dropSelf(ExampleBlocks.BLOCKS.getAllBlocks());
	}
}
