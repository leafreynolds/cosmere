/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.network.packets;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.helpers.CodecHelper;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;

public class SyncMasteryBindsMessage implements ICosmerePacket
{
	public CompoundTag data;

	public SyncMasteryBindsMessage(CompoundTag data)
	{
		this.data = data;
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeNbt(data);
	}

	public static SyncMasteryBindsMessage decode(FriendlyByteBuf buf)
	{
		return new SyncMasteryBindsMessage(buf.readNbt());
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) cap;
			CompoundTag sandmasteryTag = CompoundNBTHelper.getOrCreate(spiritweb.getCompoundTag(), "sandmastery");
			sandmasteryTag.putInt("hotkey_flags", this.data.getInt("hotkeys"));
		}));
	}

}
