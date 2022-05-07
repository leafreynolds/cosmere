/*
 * File created ~ 20 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.blocks;

import leaf.cosmere.properties.PropTypes;
import net.minecraft.world.level.block.SoundType;

public class MetalworkingTableBlock extends BaseBlock
{
	public MetalworkingTableBlock()
	{
		super(PropTypes.Blocks.METAL.get(), SoundType.METAL, 1F, 2F);
	}
}
