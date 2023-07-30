package leaf.cosmere.sandmastery.common.blocks.entities;

import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.concurrent.ThreadLocalRandom;

import static leaf.cosmere.sandmastery.common.blocks.TemporarySandBlock.AGE;

public class TemporarySandBE extends BlockEntity
{
	public TemporarySandBE(BlockPos pPos, BlockState pBlockState)
	{
		super(SandmasteryBlockEntitiesRegistry.TEMPORARY_SAND_BE.get(), pPos, pBlockState);
	}

	private int ticksSinceUpdate = 0;

	@Override
	protected void saveAdditional(CompoundTag nbt)
	{
		super.saveAdditional(nbt);
	}

	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, TemporarySandBE entity)
	{
		if (level.isClientSide()) return;
		entity.ticksSinceUpdate++;
		if (entity.ticksSinceUpdate < 5) return;
		if (ThreadLocalRandom.current().nextBoolean()) return;
		int age = state.getValue(AGE);
		if (age <= 0)
		{
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		} else
		{
			level.setBlockAndUpdate(pos, state.setValue(AGE, age - 1));
		}
		entity.ticksSinceUpdate = 0;
	}
}
