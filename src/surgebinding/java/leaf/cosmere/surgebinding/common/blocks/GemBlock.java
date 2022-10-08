/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.blocks;

import leaf.cosmere.api.IHasGemType;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.blocks.BaseBlock;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.world.level.block.SoundType;

public class GemBlock extends BaseBlock implements IHasGemType
{
	private final Roshar.Gemstone gemType;

	public GemBlock(Roshar.Gemstone gemType)
	{
		super(PropTypes.Blocks.METAL.get(), SoundType.METAL, 3F, 3F);
		this.gemType = gemType;
	}

	@Override
	public Roshar.Gemstone getGemType()
	{
		return gemType;
	}
}
