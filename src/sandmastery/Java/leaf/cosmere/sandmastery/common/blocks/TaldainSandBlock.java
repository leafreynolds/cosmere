/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.common.blocks.BaseFallingBlock;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class TaldainSandBlock extends BaseFallingBlock
{

	public TaldainSandBlock() {
		super(PropTypes.Blocks.SAND.get(), SoundType.SAND, 1F, 2F);
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return true;
	}

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!pLevel.isAreaLoaded(pPos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
		for(int i = 0; i < 4; ++i) {
			BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(5) - 3, pRandom.nextInt(3) - 1);
			System.out.println(blockpos);
			if (pLevel.getBlockState(blockpos).is(Blocks.SAND)) {
				pLevel.setBlock(blockpos, pState, 3);
			}
		}
	}
}
