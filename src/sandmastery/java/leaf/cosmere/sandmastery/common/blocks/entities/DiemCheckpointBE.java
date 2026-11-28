package leaf.cosmere.sandmastery.common.blocks.entities;

import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// holds the checkpoint's par time; change it in game with /data merge block <pos> {par:12}
public class DiemCheckpointBE extends BlockEntity
{
	public static final int DEFAULT_PAR_SECONDS = 10;
	private static final String PAR_TAG = "par";

	private int par = DEFAULT_PAR_SECONDS;

	public DiemCheckpointBE(BlockPos pos, BlockState state)
	{
		super(SandmasteryBlockEntitiesRegistry.DIEM_CHECKPOINT_BE.get(), pos, state);
	}

	// seconds this checkpoint's part of the default route should take
	public int getPar()
	{
		return par;
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);
		par = tag.contains(PAR_TAG) ? tag.getInt(PAR_TAG) : DEFAULT_PAR_SECONDS;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		tag.putInt(PAR_TAG, par);
	}
}
