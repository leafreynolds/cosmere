/*
 * File created ~ 8 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.helpers;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class CodecHelper
{
    public static Codec<List<BlockPos>> BlockPosListCodec = Codec.list(BlockPos.CODEC);

}
