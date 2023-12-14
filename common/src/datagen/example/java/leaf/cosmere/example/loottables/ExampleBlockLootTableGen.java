/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.example.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.example.common.registries.ExampleBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;

public class ExampleBlockLootTableGen extends BlockLoot
{
	@Override
	protected void addTables()
	{
		for (IBlockProvider itemRegistryObject : ExampleBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			//if (block instanceof MetalOreBlock oreBlock)
			//{
			//	this.add(oreBlock, (ore) ->
			//	{
			//		return createOreDrop(ore, ExampleItems.ITEM.get());
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
		return ExampleBlocks.BLOCKS.getAllBlocks().stream().map(IBlockProvider::getBlock)::iterator;
	}
}
