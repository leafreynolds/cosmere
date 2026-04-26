/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery.common.network.packets;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncMasteryBindsMessage(int flags) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<SyncMasteryBindsMessage> TYPE =
			new CustomPacketPayload.Type<>(Sandmastery.rl("sync_mastery_binds"));

	public static final StreamCodec<FriendlyByteBuf, SyncMasteryBindsMessage> STREAM_CODEC =
			StreamCodec.composite(
					net.minecraft.network.codec.ByteBufCodecs.INT, SyncMasteryBindsMessage::flags,
					SyncMasteryBindsMessage::new
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
		context.enqueueWork(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) cap;
			SandmasterySpiritwebSubmodule sb = (SandmasterySpiritwebSubmodule) spiritweb.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
			sb.updateFlags(this.flags);
		}));
	}
}
