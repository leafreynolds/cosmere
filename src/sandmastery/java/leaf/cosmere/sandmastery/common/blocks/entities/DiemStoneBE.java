package leaf.cosmere.sandmastery.common.blocks.entities;

import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

// Each player normally sees the stone's sand for their own Diem state (see DiemStoneRenderer).
// A shared look (a trial or an introduction in progress) is shown to everyone instead.
public class DiemStoneBE extends BlockEntity
{
	public enum Look
	{
		EMPTY, WHITE, BLACK, GLOWING
	}

	private static final String LOOK_TAG = "look";
	private static final String UNTIL_TAG = "until";

	@Nullable
	private Look shared;
	// game time the shared look ends, or 0 to keep it until cleared
	private long sharedUntil;

	public DiemStoneBE(BlockPos pos, BlockState state)
	{
		super(SandmasteryBlockEntitiesRegistry.DIEM_STONE_BE.get(), pos, state);
	}

	/**
	 * Shows a look to every player, or clears it with null. Does nothing if there's no loaded stone at pos.
	 *
	 * @param ticks how long to show it, or 0 for until cleared
	 */
	public static void setShared(Level level, BlockPos pos, @Nullable Look look, int ticks)
	{
		if (level.isLoaded(pos) && level.getBlockEntity(pos) instanceof DiemStoneBE stone)
		{
			stone.shared = look;
			stone.sharedUntil = ticks > 0 ? level.getGameTime() + ticks : 0;
			level.sendBlockUpdated(pos, stone.getBlockState(), stone.getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	@Nullable
	public Look getShared(long gameTime)
	{
		boolean expired = sharedUntil != 0 && gameTime >= sharedUntil;
		return expired ? null : shared;
	}

	// the shared look is only sent to clients, never saved, so a restart can't leave a stone stuck mid-trial.
	// Always written, even when cleared: clients ignore an empty update tag, which would leave the old look showing.
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries)
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt(LOOK_TAG, shared == null ? -1 : shared.ordinal());
		tag.putLong(UNTIL_TAG, sharedUntil);
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);
		int look = tag.contains(LOOK_TAG) ? tag.getInt(LOOK_TAG) : -1;
		shared = look < 0 ? null : Look.values()[look];
		sharedUntil = tag.getLong(UNTIL_TAG);
	}
}
