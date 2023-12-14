/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aondor.loottables;

import leaf.cosmere.aondor.common.registries.AonDorBlocks;
import leaf.cosmere.api.providers.IBlockProvider;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;

public class AonDorBlockLootTableGen extends BlockLoot
{
	@Override
	protected void addTables()
	{
		for (IBlockProvider itemRegistryObject : AonDorBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			//if (block instanceof MetalOreBlock oreBlock)
			//{
			//	this.add(oreBlock, (ore) ->
			//	{
			//		return createOreDrop(ore, AonDorItems.ITEM.get());
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
		return AonDorBlocks.BLOCKS.getAllBlocks().stream().map(IBlockProvider::getBlock)::iterator;
	}
}
