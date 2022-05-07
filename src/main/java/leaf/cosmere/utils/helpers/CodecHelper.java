/*
 * File created ~ 8 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;

import java.util.List;

public class CodecHelper
{
	public static Codec<List<BlockPos>> BlockPosListCodec = Codec.list(BlockPos.CODEC);

}
