/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.network.packets;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.helpers.CodecHelper;
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

public class SyncPushPullMessage implements ICosmerePacket
{
	public CompoundTag data;

	public SyncPushPullMessage(CompoundTag data)
	{
		this.data = data;
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeNbt(data);
	}

	public static SyncPushPullMessage decode(FriendlyByteBuf buf)
	{
		return new SyncPushPullMessage(buf.readNbt());
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) cap;

			final String pushBlocks = "pushBlocks";
			final String pullBlocks = "pullBlocks";

			boolean isPushMessage = data.contains(pushBlocks);
			String target = isPushMessage ? pushBlocks : pullBlocks;

			CodecHelper.BlockPosListCodec.decode(NbtOps.INSTANCE,
							data.getList(target, net.minecraft.nbt.Tag.TAG_INT_ARRAY))
					.resultOrPartial(CosmereAPI.logger::error)
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

			if (isPushMessage)
			{
				spiritweb.pushEntities.clear();
				final int[] pushEntities = data.getIntArray("pushEntities");
				for (int id : pushEntities)
				{
					spiritweb.pushEntities.add(id);
				}
			}
			else
			{
				spiritweb.pullEntities.clear();
				final int[] pullEntities = data.getIntArray("pullEntities");
				for (int id : pullEntities)
				{
					spiritweb.pullEntities.add(id);
				}
			}
		}));
	}

}
