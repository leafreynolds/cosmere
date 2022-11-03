/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.common.blocks.BaseFallingBlock;
import leaf.cosmere.common.properties.PropTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;

public class TaldainSandBlock extends BaseFallingBlock
{

	public TaldainSandBlock() {
		super(PropTypes.Blocks.SAND.get(), SoundType.SAND, 1F, 2F);
	}

	public static final BooleanProperty INVESTED = BooleanProperty.create("invested");


	//This somehow breaks atium metalminds and spikes?!
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		this.registerDefaultState(
				this.stateDefinition.any()
						.setValue(INVESTED, false)
		);
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return true;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
		if(blockstate.is(this)) return blockstate.setValue(INVESTED, false);
		else return super.getStateForPlacement(pContext);
	}

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		if (!pLevel.isAreaLoaded(pPos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
		if(!pLevel.canSeeSky(pPos.above())) return;
		if(pState.is(this)) pState.setValue(INVESTED, true);
		for(int i = 0; i < 2; ++i) {
			BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(5) - 3, pRandom.nextInt(3) - 1);
			if (pLevel.getBlockState(blockpos).is(Blocks.SAND)) {
				pLevel.setBlock(blockpos, pState, 3);
			}
		}
	}
}
