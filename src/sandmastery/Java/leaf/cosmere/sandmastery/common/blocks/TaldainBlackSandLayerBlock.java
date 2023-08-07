package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.common.blocks.BaseFallingBlock;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class TaldainBlackSandLayerBlock extends BaseFallingBlock
{
	public TaldainBlackSandLayerBlock()
	{
		super(PropTypes.Blocks.SAND.get(), SoundType.SAND, 1F, 2F);
		this.registerDefaultState(
				this.stateDefinition.any()
						.setValue(LAYERS, 1)
		);
	}

	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{Shapes.empty(), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	@Override
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType)
	{
		switch (pType)
		{
			case LAND:
				return pState.getValue(LAYERS) < 5;
			case WATER:
				return false;
			case AIR:
				return false;
			default:
				return false;
		}
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return SHAPE_BY_LAYER[pState.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return SHAPE_BY_LAYER[pState.getValue(LAYERS) - 1];
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos)
	{
		return SHAPE_BY_LAYER[pState.getValue(LAYERS)];
	}

	@Override
	public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext)
	{
		return SHAPE_BY_LAYER[pState.getValue(LAYERS)];
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState pState)
	{
		return true;
	}

	@Override
	public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos)
	{
		return pState.getValue(LAYERS) == 8 ? 0.2F : 1.0F;
	}

	/**
	 * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific direction passed in.
	 */
	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos)
	{
		if (touchesLiquid(pLevel, pCurrentPos, pState))
			return this.defaultBlockState().setValue(LAYERS, pState.getValue(LAYERS));
		return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
	}

	@Override
	public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext)
	{
		int i = pState.getValue(LAYERS);
		if (pUseContext.getItemInHand().is(this.asItem()) && i < 8)
		{
			if (pUseContext.replacingClickedOnBlock())
			{
				return pUseContext.getClickedFace() == Direction.UP;
			}
			else
			{
				return true;
			}
		}
		return false;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext)
	{
		BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
		if (blockstate.is(this))
		{
			int i = blockstate.getValue(LAYERS);
			return blockstate.setValue(LAYERS, Integer.valueOf(Math.min(8, i + 1)));
		}
		else
		{
			return super.getStateForPlacement(pContext);
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder
				.add(LAYERS);
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
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
		BlockState defaultWhiteSandLayers = SandmasteryBlocksRegistry.TALDAIN_WHITE_SAND_LAYER.getBlock().defaultBlockState();

		boolean nearbyInvestiture = MiscHelper.checkIfNearbyInvestiture(pLevel, pPos, false);
		boolean offTaldain = !MiscHelper.onTaldain(pLevel);
		boolean canSeeSky = pLevel.canSeeSky(pPos.above());
		if (offTaldain && !nearbyInvestiture) return;
		if (!canSeeSky && !nearbyInvestiture)
			return; // Can't see the taldanian sky nor can I find any investiture, can't charge from it

		if ((!MiscHelper.onTaldain(pLevel) && !pLevel.canSeeSky(pPos.above())) && !MiscHelper.checkIfNearbyInvestiture(pLevel, pPos, false))
			return; // Can't see the taldanian sky nor can I find any investiture, can't charge from it
		pLevel.setBlockAndUpdate(pPos, defaultWhiteSandLayers.setValue(LAYERS, pState.getValue(LAYERS)));

		for (int i = 0; i < 4; ++i)
		{
			BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
			if (pLevel.getBlockState(blockpos).is(Blocks.SAND))
			{
				pLevel.setBlockAndUpdate(blockpos, SandmasteryBlocksRegistry.TALDAIN_WHITE_SAND.getBlock().defaultBlockState());
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
}