/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;

public class BaseFallingBlock extends FallingBlock
{
	public static final MapCodec<BaseFallingBlock> CODEC = simpleCodec(BaseFallingBlock::new);

	public BaseFallingBlock(Properties properties, SoundType sound, float hardness, float resistance)
	{
		super(properties.sound(sound).strength(hardness, resistance));
	}

	public BaseFallingBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public MapCodec<? extends FallingBlock> codec()
	{
		return CODEC;
	}
}
