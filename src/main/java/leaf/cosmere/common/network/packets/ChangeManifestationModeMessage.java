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

public record ChangeManifestationModeMessage(Manifestation manifestation, int modifier) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<ChangeManifestationModeMessage> TYPE =
			new CustomPacketPayload.Type<>(Cosmere.rl("change_manifestation_mode"));

	public static final StreamCodec<ByteBuf, ChangeManifestationModeMessage> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.STRING_UTF8, msg -> msg.manifestation.getRegistryName().toString(),
					ByteBufCodecs.VAR_INT, ChangeManifestationModeMessage::modifier,
					(location, mod) -> new ChangeManifestationModeMessage(ManifestationRegistry.fromID(location), mod)
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
				SpiritwebCapability.get(sender).ifPresent((data) ->
				{
					int finalModifier = manifestation.getModeModifier(data, manifestation, modifier);
					if (finalModifier == 1)
					{
						data.nextMode(manifestation);
					}
					else if (finalModifier == -1)
					{
						data.previousMode(manifestation);
					}
					else if (finalModifier != 0)
					{
						int newMode = finalModifier + manifestation.getMode(data);
						data.setMode(manifestation, newMode);
					}
					data.syncToClients(null);
				}));
	}
}
