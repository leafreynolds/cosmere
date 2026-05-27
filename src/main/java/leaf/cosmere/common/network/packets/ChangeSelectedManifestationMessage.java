/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ChangeSelectedManifestationMessage(int dir) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<ChangeSelectedManifestationMessage> TYPE =
			new CustomPacketPayload.Type<>(Cosmere.rl("change_selected_manifestation"));

	public static final StreamCodec<ByteBuf, ChangeSelectedManifestationMessage> STREAM_CODEC =
			ByteBufCodecs.VAR_INT.map(ChangeSelectedManifestationMessage::new, ChangeSelectedManifestationMessage::dir);

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
					cap.changeManifestation(dir);
					cap.syncToClients(null);
				}));
	}
}
