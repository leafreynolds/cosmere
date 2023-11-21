/*
 * File updated ~ 17 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.common.blocks;

import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class BaseBlock extends Block
{

	public BaseBlock()
	{
		this(PropTypes.Blocks.METAL.get(), SoundType.METAL, 1F, 2F);
	}

	public BaseBlock(Properties properties, SoundType sound, float hardness, float resistance)
	{
		super(properties.sound(sound).strength(hardness, resistance));
	}

	public BaseBlock(Properties properties)
	{
		super(properties);

	}
}
