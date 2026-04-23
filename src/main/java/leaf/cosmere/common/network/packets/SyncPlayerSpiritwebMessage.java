/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncPlayerSpiritwebMessage(int entityID, CompoundTag entityNBT) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<SyncPlayerSpiritwebMessage> TYPE =
			new CustomPacketPayload.Type<>(Cosmere.rl("sync_player_spiritweb"));

	public static final StreamCodec<ByteBuf, SyncPlayerSpiritwebMessage> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.INT, SyncPlayerSpiritwebMessage::entityID,
					ByteBufCodecs.TRUSTED_COMPOUND_TAG, SyncPlayerSpiritwebMessage::entityNBT,
					SyncPlayerSpiritwebMessage::new
			);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		context.enqueueWork(() ->
		{
			Entity result = Minecraft.getInstance().level.getEntity(entityID);
			if (result != null)
			{
				SpiritwebCapability.get((LivingEntity) result).ifPresent((c) ->
				{
					c.deserializeNBT(result.level().registryAccess(), entityNBT);
					c.getLiving().refreshDimensions();
				});
			}
		});
	}
}
