/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.soulforgery.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.loottables.BaseBlockLootTables;
import leaf.cosmere.soulforgery.common.registries.SoulforgeryBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;

public class SoulforgeryBlockLootTableGen extends BaseBlockLootTables
{
	public SoulforgeryBlockLootTableGen(HolderLookup.Provider provider)
	{
		super(provider);
	}

	@Override
	protected void generate()
	{
		//first catch any blocks that don't drop self, like ores
		for (IBlockProvider itemRegistryObject : SoulforgeryBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			//if (block instanceof MetalOreBlock oreBlock)
			//{
			//      this.add(oreBlock, (ore) -> createOreDrop(ore, ItemsRegistry.METAL_RAW_ORE.get(oreBlock.getMetalType())));
			//}
		}

		//then make the rest drop themselves.
		dropSelf(SoulforgeryBlocks.BLOCKS.getAllBlocks());
	}

	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return SoulforgeryBlocks.BLOCKS.getAllBlocks().stream().map(IBlockProvider::getBlock)::iterator;
	}
}
