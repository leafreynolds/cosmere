/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.common.resource.ore;

import leaf.cosmere.common.blocks.MetalOreBlock;
import leaf.cosmere.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.item.BlockItem;

public record OreBlockType(BlockRegistryObject<MetalOreBlock, BlockItem> stone,
                           BlockRegistryObject<MetalOreBlock, BlockItem> deepslate)
{

	public MetalOreBlock stoneBlock()
	{
		return stone.getBlock();
	}

	public MetalOreBlock deepslateBlock()
	{
		return deepslate.getBlock();
	}
}