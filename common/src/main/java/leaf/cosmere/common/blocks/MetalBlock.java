/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.blocks;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.world.level.block.SoundType;

public class MetalBlock extends BaseBlock implements IHasMetalType
{
	private final Metals.MetalType metalType;

	public MetalBlock(Metals.MetalType metalType)
	{
		super(PropTypes.Blocks.METAL.get(), SoundType.METAL, 3F, 3F);
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}
}
