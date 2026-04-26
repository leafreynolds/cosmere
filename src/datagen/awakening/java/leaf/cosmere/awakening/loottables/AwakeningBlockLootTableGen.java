/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.awakening.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.awakening.common.registries.AwakeningBlocks;
import leaf.cosmere.loottables.BaseBlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;

public class AwakeningBlockLootTableGen extends BaseBlockLootTables
{
	public AwakeningBlockLootTableGen(HolderLookup.Provider provider)
	{
		super(provider);
	}

	@Override
	protected void generate()
	{
		//first catch any blocks that don't drop self, like ores
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
		}

		//then make the rest drop themselves.
		dropSelf(AwakeningBlocks.BLOCKS.getAllBlocks());
	}
}
