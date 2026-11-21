package leaf.cosmere.common.network;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public abstract class BasePacketHandler
{
	public void register(IEventBus modBus)
	{
		modBus.addListener(this::onRegisterPayloadHandlers);
	}

	private void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event)
	{
		initialize(event.registrar(getProtocolVersion()));
	}

	protected abstract String getProtocolVersion();

	public abstract void initialize(PayloadRegistrar registrar);

	/**
	 * Sends a packet to the server.<br>
	 * Must be called Client side.
	 */
	public void sendToServer(ICosmerePacket msg)
	{
		PacketDistributor.sendToServer(msg);
	}

	/**
	 * Send a packet to a specific player.<br>
	 * Must be called Server side.
	 */
	public void sendTo(ICosmerePacket msg, ServerPlayer player)
	{
		if (!(player instanceof FakePlayer))
		{
			PacketDistributor.sendToPlayer(player, msg);
		}
	}

	public void sendToAllInWorld(ICosmerePacket mes, ServerLevel world)
	{
		PacketDistributor.sendToPlayersInDimension(world, mes);
	}
}
