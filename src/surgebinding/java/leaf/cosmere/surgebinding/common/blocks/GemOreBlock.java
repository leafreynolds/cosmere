/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.blocks;

import leaf.cosmere.api.IHasGemType;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;

public class GemOreBlock extends DropExperienceBlock implements IHasGemType
{
	private final Roshar.Gemstone gemType;

	public GemOreBlock(Roshar.Gemstone gemType)
	{
		super(PropTypes.Blocks.ORE.get().strength(3f, 3f).requiresCorrectToolForDrops(), UniformInt.of(0, 2));
		this.gemType = gemType;
	}

	@Override
	public Roshar.Gemstone getGemType()
	{
		return gemType;
	}
}
