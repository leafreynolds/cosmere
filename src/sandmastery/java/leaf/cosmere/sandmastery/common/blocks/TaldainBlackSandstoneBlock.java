package leaf.cosmere.sandmastery.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class TaldainBlackSandstoneBlock extends Block
{
	private final Supplier<? extends Block> charged;

	public TaldainBlackSandstoneBlock(Supplier<? extends Block> charged)
	{
		super(SandstoneCharging.properties());
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
		SandstoneCharging.tryCharge(pState, pLevel, pPos, pRandom, charged.get());
	}
}
