/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;

import java.util.List;

public class CodecHelper
{
	public static Codec<List<BlockPos>> BlockPosListCodec = Codec.list(BlockPos.CODEC);

}
