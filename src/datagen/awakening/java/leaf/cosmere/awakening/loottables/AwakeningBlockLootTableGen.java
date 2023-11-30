/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.awakening.common.registries.AwakeningBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;

public class AwakeningBlockLootTableGen extends BlockLoot
{
	@Override
	protected void addTables()
	{
		for (IBlockProvider itemRegistryObject : AwakeningBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			//if (block instanceof MetalOreBlock oreBlock)
			//{
			//	this.add(oreBlock, (ore) ->
			//	{
			//		return createOreDrop(ore, AwakeningItems.ITEM.get());
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
		return AwakeningBlocks.BLOCKS.getAllBlocks().stream().map(IBlockProvider::getBlock)::iterator;
	}
}
