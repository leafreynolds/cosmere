/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.helpers.CodecHelper;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record SyncPushPullMessage(CompoundTag data) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<SyncPushPullMessage> TYPE =
			new CustomPacketPayload.Type<>(Cosmere.rl("sync_push_pull"));

	public static final StreamCodec<ByteBuf, SyncPushPullMessage> STREAM_CODEC =
			ByteBufCodecs.COMPOUND_TAG.map(SyncPushPullMessage::new, SyncPushPullMessage::data);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		if (!(context.player() instanceof ServerPlayer sender))
		{
			return;
		}
		context.enqueueWork(() ->
				SpiritwebCapability.get(sender).ifPresent((cap) ->
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

					spiritweb.pushPullWeight = data.getInt("weight");
				}));
	}
}
