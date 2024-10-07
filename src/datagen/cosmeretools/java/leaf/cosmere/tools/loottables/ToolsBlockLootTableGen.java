/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.loottables.BaseBlockLootTables;
import leaf.cosmere.tools.common.registries.ToolsBlocks;
import net.minecraft.world.level.block.Block;

public class ToolsBlockLootTableGen extends BaseBlockLootTables
{
	@Override
	protected void generate()
	{
		for (IBlockProvider itemRegistryObject : ToolsBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			//if (block instanceof MetalOreBlock oreBlock)
			//{
			//      this.add(oreBlock, (ore) -> createOreDrop(ore, ItemsRegistry.METAL_RAW_ORE.get(oreBlock.getMetalType())));
			//}
		}

		//then make the rest drop themselves.
		dropSelf(ToolsBlocks.BLOCKS.getAllBlocks());
	}
}
