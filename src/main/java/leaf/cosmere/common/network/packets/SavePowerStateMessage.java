/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
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

public record SavePowerStateMessage(int powerState) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<SavePowerStateMessage> TYPE =
			new CustomPacketPayload.Type<>(Cosmere.rl("save_power_state"));

	public static final StreamCodec<ByteBuf, SavePowerStateMessage> STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.VAR_INT, SavePowerStateMessage::powerState,
					SavePowerStateMessage::new
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
						data.saveNewState(powerState)));
	}
}
