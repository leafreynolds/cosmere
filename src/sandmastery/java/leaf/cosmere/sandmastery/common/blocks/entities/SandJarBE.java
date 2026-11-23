package leaf.cosmere.sandmastery.common.blocks.entities;

import leaf.cosmere.sandmastery.common.blocks.WaterDripperBlock;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import leaf.cosmere.sandmastery.common.utils.MiscHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static leaf.cosmere.sandmastery.common.blocks.SandJarBlock.GLOWING;
import static leaf.cosmere.sandmastery.common.blocks.SandJarBlock.INVESTITURE;

public class SandJarBE extends BlockEntity
{
	public SandJarBE(BlockPos pPos, BlockState pBlockState)
	{
		super(SandmasteryBlockEntitiesRegistry.SAND_JAR_BE.get(), pPos, pBlockState);
	}

	private int ticksSinceUpdate = 0;

	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries)
	{
		super.saveAdditional(nbt, registries);
	}

	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries)
	{
		super.loadAdditional(nbt, registries);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, SandJarBE entity)
	{
		if (level.isClientSide()) return;
		entity.ticksSinceUpdate++;
		if (entity.ticksSinceUpdate < 4) return;
		entity.ticksSinceUpdate = 0;

		// A water dripper overrides detection: glow while dripped on, then go back to black sand once it stops
		boolean glowing = state.getValue(GLOWING);
		if (WaterDripperBlock.isDrippingOnto(level, pos))
		{
			if (!glowing)
			{
				level.setBlockAndUpdate(pos, state.setValue(GLOWING, true));
			}
			return;
		}
		if (glowing)
		{
			level.setBlockAndUpdate(pos, state.setValue(GLOWING, false).setValue(INVESTITURE, 0));
			return;
		}

		int investiture = state.getValue(INVESTITURE);
		if (MiscHelper.checkIfNearbyInvestiture((ServerLevel) level, pos, true))
			level.setBlockAndUpdate(pos, state.setValue(INVESTITURE, Math.min(investiture + 1, 100)));
		else
			level.setBlockAndUpdate(pos, state.setValue(INVESTITURE, Math.max(investiture - 4, 0)));
	}
}
