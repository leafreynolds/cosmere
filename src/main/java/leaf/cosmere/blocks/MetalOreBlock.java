/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.blocks;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.world.level.block.OreBlock;

public class MetalOreBlock extends OreBlock implements IHasMetalType
{
	private final Metals.MetalType metalType;

	public MetalOreBlock(Metals.MetalType metalType)
	{
		super(PropTypes.Blocks.METAL.get());
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}
}
