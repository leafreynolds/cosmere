/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.blocks.MetalOreBlock;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;

public class SandmasteryBlockLootTableGen extends BlockLoot
{
	@Override
	protected void addTables()
	{
		for (IBlockProvider itemRegistryObject : SandmasteryBlocksRegistry.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			this.dropSelf(block);

		}
	}

	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return SandmasteryBlocksRegistry.BLOCKS.getAllBlocks().stream().map(IBlockProvider::getBlock)::iterator;
	}
}
