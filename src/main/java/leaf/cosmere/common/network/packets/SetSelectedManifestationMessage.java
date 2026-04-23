/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.common.registry.ManifestationRegistry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSelectedManifestationMessage(Manifestation manifestation) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<SetSelectedManifestationMessage> TYPE =
			new CustomPacketPayload.Type<>(Cosmere.rl("set_selected_manifestation"));

	public static final StreamCodec<ByteBuf, SetSelectedManifestationMessage> STREAM_CODEC =
			ByteBufCodecs.STRING_UTF8.map(
					location -> new SetSelectedManifestationMessage(ManifestationRegistry.fromID(location)),
					msg -> msg.manifestation.getRegistryName().toString()
			);

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
					cap.setSelectedManifestation(manifestation);
					cap.syncToClients(null);
				}));
	}
}
