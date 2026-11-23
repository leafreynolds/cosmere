package leaf.cosmere.sandmastery.common.blocks;

import com.mojang.serialization.MapCodec;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WaterDripperBlock extends Block
{
	public static final MapCodec<WaterDripperBlock> CODEC = simpleCodec(WaterDripperBlock::new);
	public static final int MAX_AIR_GAP = 4;

	public WaterDripperBlock(Properties properties)
	{
		super(properties);
	}

	public WaterDripperBlock()
	{
		this(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
	}

	/** Matches the upside-down lantern model: the cage, then the neck below it */
	private static final VoxelShape SHAPE = Shapes.or(
			Block.box(5, 9, 5, 11, 16, 11),
			Block.box(6, 7, 6, 10, 9, 10));

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
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
			BlockState state = level.getBlockState(targetPos.above(i));
			if (state.is(SandmasteryBlocks.WATER_DRIPPER.getBlock()))
			{
				return true;
			}
			if (!state.isAir())
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
				pos.getY() + 5 / 16.0 - 0.05, // from the lowest point of the model
				pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2,
				0, 0, 0);
	}
}
