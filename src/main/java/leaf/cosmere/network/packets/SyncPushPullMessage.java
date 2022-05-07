/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.utils.helpers.CodecHelper;
import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SyncPushPullMessage
{
	public CompoundTag data;

	public SyncPushPullMessage(CompoundTag data)
	{
		this.data = data;
	}

	public static void encode(SyncPushPullMessage mes, FriendlyByteBuf buf)
	{
		buf.writeNbt(mes.data);
	}

	public static SyncPushPullMessage decode(FriendlyByteBuf buf)
	{
		return new SyncPushPullMessage(buf.readNbt());
	}

	public static void handle(SyncPushPullMessage mes, Supplier<NetworkEvent.Context> cont)
	{

		NetworkEvent.Context context = cont.get();
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) cap;

			final String pushBlocks = "pushBlocks";
			final String pullBlocks = "pullBlocks";

			boolean isPushMessage = mes.data.contains(pushBlocks);
			String target = isPushMessage ? pushBlocks : pullBlocks;

			CodecHelper.BlockPosListCodec.decode(NbtOps.INSTANCE,
					mes.data.getList(target, net.minecraft.nbt.Tag.TAG_INT_ARRAY))
					.resultOrPartial(LogHelper.LOGGER::error)
					.ifPresent(listINBTPair ->
					{
						List<BlockPos> messageBlocks = listINBTPair.getFirst();

						if (isPushMessage)
						{
							spiritweb.pushBlocks.clear();
							spiritweb.pushBlocks.addAll(messageBlocks);
						}
						else
						{
							spiritweb.pullBlocks.clear();
							spiritweb.pullBlocks.addAll(messageBlocks);
						}
					});
		}));
		cont.get().setPacketHandled(true);
	}

}
