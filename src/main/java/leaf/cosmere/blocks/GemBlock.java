/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.blocks;

import leaf.cosmere.constants.Roshar;
import leaf.cosmere.items.IHasGemType;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.world.level.block.SoundType;

public class GemBlock extends BaseBlock implements IHasGemType
{
	private final Roshar.Polestone gemType;

	public GemBlock(Roshar.Polestone gemType)
	{
		super(PropTypes.Blocks.METAL.get(), SoundType.METAL, 3F, 3F);
		this.gemType = gemType;
	}

	@Override
	public Roshar.Polestone getGemType()
	{
		return gemType;
	}
}
