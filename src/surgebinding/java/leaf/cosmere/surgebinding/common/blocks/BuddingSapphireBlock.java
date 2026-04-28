package leaf.cosmere.surgebinding.common.blocks;

import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class BuddingSapphireBlock extends BuddingAmethystBlock
{
	public BuddingSapphireBlock(Properties pProperties)
	{
		super(pProperties);
	}

	private static final Direction[] DIRECTIONS = Direction.values();

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
	{
		if (pRandom.nextInt(5) == 0)
		{
			Direction direction = DIRECTIONS[pRandom.nextInt(DIRECTIONS.length)];
			BlockPos blockpos = pPos.relative(direction);
			BlockState blockstate = pLevel.getBlockState(blockpos);
			Block block = null;
			if (canClusterGrowAtState(blockstate))
			{
				block = SurgebindingBlocks.SMALL_SAPPHIRE_BUD.getBlock();
			}
			else if (blockstate.is(SurgebindingBlocks.SMALL_SAPPHIRE_BUD.getBlock()) && blockstate.getValue(SapphireClusterBlock.FACING) == direction)
			{
				block = SurgebindingBlocks.MEDIUM_SAPPHIRE_BUD.getBlock();
			}
			else if (blockstate.is(SurgebindingBlocks.MEDIUM_SAPPHIRE_BUD.getBlock()) && blockstate.getValue(SapphireClusterBlock.FACING) == direction)
			{
				block = SurgebindingBlocks.LARGE_SAPPHIRE_BUD.getBlock();
			}
			else if (blockstate.is(SurgebindingBlocks.LARGE_SAPPHIRE_BUD.getBlock()) && blockstate.getValue(SapphireClusterBlock.FACING) == direction)
			{
				block = SurgebindingBlocks.SAPPHIRE_CLUSTER.getBlock();
			}

			if (block != null)
			{
				BlockState blockstate1 = block.defaultBlockState().setValue(SapphireClusterBlock.FACING, direction).setValue(SapphireClusterBlock.WATERLOGGED, Boolean.valueOf(blockstate.getFluidState().getType() == Fluids.WATER));
				pLevel.setBlockAndUpdate(blockpos, blockstate1);
			}
		}
	}
}
