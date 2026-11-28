package leaf.cosmere.sandmastery.common.blocks;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WaterDripperBlock extends Block
{
	public static final MapCodec<WaterDripperBlock> CODEC = simpleCodec(WaterDripperBlock::new);
	public static final int MAX_AIR_GAP = 4;
	// DOWN hangs from the ceiling; a horizontal direction is wall mounted, pointing away from the wall
	public static final DirectionProperty FACING = DirectionProperty.create("facing", direction -> direction != Direction.UP);

	public WaterDripperBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.DOWN));
	}

	public WaterDripperBlock()
	{
		this(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
	}

	// the basin sits in the centre of both the hanging and wall models, so one shape fits every facing
	private static final VoxelShape SHAPE = Shapes.or(
			Block.box(5, 7, 5, 11, 11, 11),
			Block.box(6, 6, 6, 10, 7, 10));

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}

	// placement follows hanging signs (HangingSignItem): try the directions the player is looking, nearest first,
	// never the floor; looking up hangs it from the ceiling, looking at a side mounts it on that wall
	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
	{
		for (Direction direction : context.getNearestLookingDirections())
		{
			if (direction == Direction.DOWN)
			{
				continue;
			}
			final BlockState state = defaultBlockState().setValue(FACING, direction == Direction.UP ? Direction.DOWN : direction.getOpposite());
			if (state.canSurvive(context.getLevel(), context.getClickedPos()))
			{
				return state;
			}
		}
		return null;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		final Direction toSupport = supportDirection(state);
		final BlockPos supportPos = pos.relative(toSupport);
		final BlockState support = level.getBlockState(supportPos);
		// same checks as CeilingHangingSignBlock and WallHangingSignBlock
		return toSupport == Direction.UP
		       ? support.isFaceSturdy(level, supportPos, Direction.DOWN, SupportType.CENTER)
		       : support.isFaceSturdy(level, supportPos, toSupport.getOpposite(), SupportType.FULL);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos)
	{
		return direction == supportDirection(state) && !state.canSurvive(level, pos)
		       ? Blocks.AIR.defaultBlockState()
		       : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	private static Direction supportDirection(BlockState state)
	{
		final Direction facing = state.getValue(FACING);
		return facing == Direction.DOWN ? Direction.UP : facing.getOpposite();
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation)
	{
		return state.getValue(FACING) == Direction.DOWN ? state : state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror)
	{
		return state.getValue(FACING) == Direction.DOWN ? state : state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected MapCodec<? extends Block> codec()
	{
		return CODEC;
	}

	@Nullable
	public static BlockPos findTargetBelow(BlockGetter level, BlockPos dripperPos)
	{
		for (int i = 1; i <= MAX_AIR_GAP + 1; i++)
		{
			BlockPos pos = dripperPos.below(i);
			if (!level.getBlockState(pos).isAir())
			{
				return pos;
			}
		}
		return null;
	}

	public static boolean isDrippingOnto(BlockGetter level, BlockPos targetPos)
	{
		for (int i = 1; i <= MAX_AIR_GAP + 1; i++)
		{
			BlockPos pos = targetPos.above(i);
			BlockState state = level.getBlockState(pos);
			if (state.is(SandmasteryBlocks.WATER_DRIPPER.getBlock()))
			{
				return true;
			}
			Vec3 top = new Vec3(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			Vec3 bottom = new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			if (state.getShape(level, pos).clip(top, bottom, pos) != null)
			{
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isRandomlyTicking(BlockState state)
	{
		return true;
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		BlockPos target = findTargetBelow(level, pos);
		if (target != null && level.getBlockState(target).is(SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock()))
		{
			level.setBlockAndUpdate(target, SandmasteryBlocks.TALDAIN_GLOWING_SAND.getBlock().defaultBlockState());
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		level.addParticle(ParticleTypes.DRIPPING_WATER,
				pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2,
				pos.getY() + 5.5 / 16.0 - 0.05, // from the lowest point of the model
				pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2,
				0, 0, 0);
	}
}
