package leaf.cosmere.sandmastery.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class TaldainWhiteSandstoneBlock extends Block
{
	private final Supplier<? extends Block> uncharged;

	public TaldainWhiteSandstoneBlock(Supplier<? extends Block> uncharged)
	{
		super(SandstoneCharging.properties());
		this.uncharged = uncharged;
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos)
	{
		return SandstoneCharging.revertIfWet(super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos), pLevel, pCurrentPos, uncharged.get());
	}
}
