/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.blocks;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.world.level.block.SoundType;

public class MetalBlock extends BaseBlock implements IHasMetalType
{
	private final Metals.MetalType metalType;

	public MetalBlock(Metals.MetalType metalType)
	{
		super(PropTypes.Blocks.METAL.get(), SoundType.METAL, 1F, 2F);
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}
}
