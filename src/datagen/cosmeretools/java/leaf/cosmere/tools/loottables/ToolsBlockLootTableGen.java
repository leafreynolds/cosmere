/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.tools.common.registries.ToolsBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;

public class ToolsBlockLootTableGen extends BlockLoot
{
	@Override
	protected void addTables()
	{
		for (IBlockProvider itemRegistryObject : ToolsBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			//if (block instanceof MetalOreBlock oreBlock)
			//{
			//	this.add(oreBlock, (ore) ->
			//	{
			//		return createOreDrop(ore, ToolsItems.ITEM.get());
			//	});
			//}
			//else
			{
				this.dropSelf(block);
			}
		}
	}

	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return ToolsBlocks.BLOCKS.getAllBlocks().stream().map(IBlockProvider::getBlock)::iterator;
	}
}
