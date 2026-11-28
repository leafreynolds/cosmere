package leaf.cosmere.sandmastery.common.blocks.entities;

import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GlowingSandBE extends BlockEntity
{
	public static final int DECAY_TICKS = 20 * 30;

	private int ticksRemaining = DECAY_TICKS;

	public GlowingSandBE(BlockPos pos, BlockState state)
	{
		super(SandmasteryBlockEntitiesRegistry.GLOWING_SAND_BE.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries)
	{
		super.saveAdditional(nbt, registries);
		nbt.putInt("ticksRemaining", ticksRemaining);
	}

	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries)
	{
		super.loadAdditional(nbt, registries);
		if (nbt.contains("ticksRemaining"))
		{
			ticksRemaining = nbt.getInt("ticksRemaining");
		}
	}

	public static void tick(Level level, BlockPos pos, BlockState state, GlowingSandBE be)
	{
		if (--be.ticksRemaining <= 0)
		{
			level.setBlockAndUpdate(pos, SandmasteryBlocks.TALDAIN_BLACK_SAND.getBlock().defaultBlockState());
		}
		else if (be.ticksRemaining % 200 == 0)
		{
			be.setChanged(); // persist progress periodically rather than every tick
		}
	}
}
