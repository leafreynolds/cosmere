/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static leaf.cosmere.api.Constants.Translations.POWER_INACTIVE;

public record DeactivateManifestationsMessage() implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<DeactivateManifestationsMessage> TYPE =
			new CustomPacketPayload.Type<>(Cosmere.rl("deactivate_manifestations"));

	public static final StreamCodec<ByteBuf, DeactivateManifestationsMessage> STREAM_CODEC =
			StreamCodec.unit(new DeactivateManifestationsMessage());

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
					MutableComponent manifestationText = POWER_INACTIVE;
					cap.deactivateManifestations();
					sender.sendSystemMessage(manifestationText);
					cap.syncToClients(null);
				}));
	}
}
