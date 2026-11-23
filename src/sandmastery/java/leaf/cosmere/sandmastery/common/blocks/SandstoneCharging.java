package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

/** Charging and water rules shared by every Taldain sandstone block (full blocks, slabs, stairs, walls). */
final class SandstoneCharging
{
	// Sand charges on roughly every random tick; sandstone only passes 1 in this many of them.
	private static final int CHARGE_RESISTANCE = 10;

	private SandstoneCharging() {}

	static BlockBehaviour.Properties properties()
	{
		return BlockBehaviour.Properties.of().strength(0.8F).sound(SoundType.STONE).requiresCorrectToolForDrops();
	}

	static void tryCharge(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, Block charged)
	{
		// roll first: the investiture check below scans for entities
		if (random.nextInt(CHARGE_RESISTANCE) != 0 || !level.isAreaLoaded(pos, 3))
		{
			return;
		}

		final boolean nearbyInvestiture = MiscHelper.checkIfNearbyInvestiture(level, pos, false);
		final boolean canCharge = MiscHelper.onTaldain(level) ? level.canSeeSky(pos.above()) || nearbyInvestiture : nearbyInvestiture;
		if (canCharge)
		{
			level.setBlockAndUpdate(pos, copyProperties(state, charged));
		}
	}

	/** Returns the uncharged counterpart (same facing, half, etc.) if water touches the block, else the state unchanged. */
	static BlockState revertIfWet(BlockState state, LevelAccessor level, BlockPos pos, Block uncharged)
	{
		return MiscHelper.touchesLiquid(level, pos, state) ? copyProperties(state, uncharged) : state;
	}

	private static BlockState copyProperties(BlockState from, Block to)
	{
		BlockState result = to.defaultBlockState();
		for (Property<?> property : from.getProperties())
		{
			if (result.hasProperty(property))
			{
				result = copy(result, from, property);
			}
		}
		return result;
	}

	private static <T extends Comparable<T>> BlockState copy(BlockState to, BlockState from, Property<T> property)
	{
		return to.setValue(property, from.getValue(property));
	}
}
