/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.blocks;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;

public class MetalOreBlock extends DropExperienceBlock implements IHasMetalType
{
	private final Metals.MetalType metalType;

	public MetalOreBlock(Metals.MetalType metalType)
	{
		super(PropTypes.Blocks.ORE.get().strength(3f, 3f).requiresCorrectToolForDrops(), UniformInt.of(0, 2));
		this.metalType = metalType;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}
}
