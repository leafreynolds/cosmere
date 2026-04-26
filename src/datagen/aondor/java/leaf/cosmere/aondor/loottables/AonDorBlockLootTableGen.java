/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.aondor.loottables;

import leaf.cosmere.aondor.common.registries.AonDorBlocks;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.loottables.BaseBlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;

public class AonDorBlockLootTableGen extends BaseBlockLootTables
{
	public AonDorBlockLootTableGen(HolderLookup.Provider provider)
	{
		super(provider);
	}

	@Override
	protected void generate()
	{
		//first catch any blocks that don't drop self, like ores
		for (IBlockProvider itemRegistryObject : AonDorBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			//if (block instanceof MetalOreBlock oreBlock)
			//{
			//      this.add(oreBlock, (ore) -> createOreDrop(ore, ItemsRegistry.METAL_RAW_ORE.get(oreBlock.getMetalType())));
			//}
		}

		//then make the rest drop themselves.
		dropSelf(AonDorBlocks.BLOCKS.getAllBlocks());
	}
}
