/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.blocks;

//import leaf.cosmere.allomancy.common.registries.AllomancyEffects;

import leaf.cosmere.common.blocks.BaseFallingBlock;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class TaldainBlackSandBlock extends BaseFallingBlock
{

	public TaldainBlackSandBlock()
	{
		super(PropTypes.Blocks.SAND.get(), SoundType.SAND, 1F, 2F);
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState)
	{
		return true;
	}

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
	{
		if (!pLevel.isAreaLoaded(pPos, 3))
		{
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
		}

		boolean nearbyInvestiture = MiscHelper.checkIfNearbyInvestiture(pLevel, pPos, false);
		boolean offTaldain = !MiscHelper.onTaldain(pLevel);
		boolean canSeeSky = pLevel.canSeeSky(pPos.above());
		if (offTaldain && !nearbyInvestiture)
		{
			return;
		}
		if (!canSeeSky && !nearbyInvestiture)
		{
			return; // Can't see the taldanian sky nor can I find any investiture, can't charge from it
		}

		pLevel.setBlockAndUpdate(pPos, SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock().defaultBlockState());

		for (int i = 0; i < 4; ++i)
		{
			BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
			if (pLevel.getBlockState(blockpos).is(Blocks.SAND))
			{
				pLevel.setBlockAndUpdate(blockpos, SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock().defaultBlockState());
			}
		}
	}

	private static boolean touchesLiquid(BlockGetter pLevel, BlockPos pPos, BlockState state)
	{
		boolean touching = false;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.mutable();
		for (Direction direction : Direction.values())
		{
			BlockState blockstate = pLevel.getBlockState(blockpos$mutableblockpos);
			if (direction != Direction.DOWN || state.canBeHydrated(pLevel, pPos, blockstate.getFluidState(), blockpos$mutableblockpos))
			{
				blockpos$mutableblockpos.setWithOffset(pPos, direction);
				blockstate = pLevel.getBlockState(blockpos$mutableblockpos);
				if (state.canBeHydrated(pLevel, pPos, blockstate.getFluidState(), blockpos$mutableblockpos) && !blockstate.isFaceSturdy(pLevel, pPos, direction.getOpposite()))
				{
					touching = true;
					break;
				}
			}
		}
		return touching;
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos)
	{
		return touchesLiquid(pLevel, pCurrentPos, pState) ?
		       this.defaultBlockState() :
		       super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
	}
}
