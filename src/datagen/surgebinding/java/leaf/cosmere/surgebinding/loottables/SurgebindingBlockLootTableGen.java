/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.loottables.BaseBlockLootTables;
import leaf.cosmere.surgebinding.common.blocks.GemOreBlock;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.world.level.block.Block;

public class SurgebindingBlockLootTableGen extends BaseBlockLootTables
{
	@Override
	protected void generate()
	{
		//first catch any blocks that don't drop self, like ores
		for (IBlockProvider itemRegistryObject : SurgebindingBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			if (block instanceof GemOreBlock oreBlock)
			{
				this.add(oreBlock, (ore) -> createOreDrop(ore, SurgebindingItems.GEMSTONE_CHIPS.get(oreBlock.getGemType())));
			}
		}

		//then make the rest drop themselves.
		dropSelf(BlocksRegistry.BLOCKS.getAllBlocks());
	}
}
