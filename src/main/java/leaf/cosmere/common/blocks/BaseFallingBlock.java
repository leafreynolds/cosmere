/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;

public class BaseFallingBlock extends FallingBlock
{

	public BaseFallingBlock(Properties properties, SoundType sound, float hardness, float resistance)
	{
		super(properties.sound(sound).strength(hardness, resistance));
	}

	public BaseFallingBlock(Properties properties)
	{
		super(properties);

	}
}
