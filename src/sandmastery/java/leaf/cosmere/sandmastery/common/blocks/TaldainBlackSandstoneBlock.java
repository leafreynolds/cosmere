package leaf.cosmere.sandmastery.common.blocks;

import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class TaldainBlackSandstoneBlock extends Block
{
	// Sand charges on roughly every random tick; sandstone only passes 1 in this many of them.
	private static final int CHARGE_RESISTANCE = 10;

	private final Supplier<? extends Block> charged;

	public TaldainBlackSandstoneBlock(Supplier<? extends Block> charged)
	{
		super(Properties.of().strength(0.8F).sound(SoundType.STONE).requiresCorrectToolForDrops());
		this.charged = charged;
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState)
	{
		return true;
	}

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom)
	{
		// roll first: the investiture check below scans for entities
		if (pRandom.nextInt(CHARGE_RESISTANCE) != 0 || !pLevel.isAreaLoaded(pPos, 3))
		{
			return;
		}

		final boolean nearbyInvestiture = MiscHelper.checkIfNearbyInvestiture(pLevel, pPos, false);
		final boolean canCharge = MiscHelper.onTaldain(pLevel) ? pLevel.canSeeSky(pPos.above()) || nearbyInvestiture : nearbyInvestiture;
		if (canCharge)
		{
			pLevel.setBlockAndUpdate(pPos, charged.get().defaultBlockState());
		}
	}
}
