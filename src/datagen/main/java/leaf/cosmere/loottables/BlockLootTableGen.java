/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.blocks.MetalOreBlock;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.world.level.block.Block;

public class BlockLootTableGen extends BaseBlockLootTables
{
	@Override
	protected void generate()
	{
		for (IBlockProvider itemRegistryObject : BlocksRegistry.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			if (block instanceof MetalOreBlock oreBlock)
			{

				this.add(oreBlock, (ore) -> createOreDrop(ore, ItemsRegistry.METAL_RAW_ORE.get(oreBlock.getMetalType()).asItem()));
			}
			else
			{
				this.dropSelf(block);
			}
		}
	}
}
