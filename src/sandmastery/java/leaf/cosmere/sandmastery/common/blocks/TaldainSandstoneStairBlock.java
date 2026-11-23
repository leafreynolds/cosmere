package leaf.cosmere.sandmastery.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/** Black (uncharged) sandstone slowly charges into its white counterpart; white reverts when water touches it. */
public class TaldainSandstoneStairBlock extends StairBlock
{
	private final boolean charged;
	private final Supplier<? extends Block> counterpart;

	public TaldainSandstoneStairBlock(boolean charged, Supplier<? extends Block> base, Supplier<? extends Block> counterpart)
	{
		super(base.get().defaultBlockState(), SandstoneCharging.properties());
		this.charged = charged;
		this.counterpart = counterpart;
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState)
	{
		return !charged;
	}

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
	{
		if (!charged)
		{
			SandstoneCharging.tryCharge(pState, pLevel, pPos, pRandom, counterpart.get());
		}
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos)
	{
		final BlockState state = super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
		return charged ? SandstoneCharging.revertIfWet(state, pLevel, pCurrentPos, counterpart.get()) : state;
	}
}
