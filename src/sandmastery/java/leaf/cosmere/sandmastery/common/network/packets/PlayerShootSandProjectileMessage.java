/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery.common.network.packets;

import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PlayerShootSandProjectileMessage() implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<PlayerShootSandProjectileMessage> TYPE =
			new CustomPacketPayload.Type<>(Sandmastery.rl("player_shoot_sand_projectile"));

	public static final StreamCodec<io.netty.buffer.ByteBuf, PlayerShootSandProjectileMessage> STREAM_CODEC =
			StreamCodec.unit(new PlayerShootSandProjectileMessage());

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		if (!(context.player() instanceof net.minecraft.server.level.ServerPlayer))
		{
			return;
		}
		context.enqueueWork(() ->
		{
			// TODO: server-side shoot logic
		});
	}
}
