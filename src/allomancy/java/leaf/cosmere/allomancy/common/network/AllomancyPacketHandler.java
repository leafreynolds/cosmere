/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.allomancy.common.network;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.network.packets.EntityAllomancyActivateMessage;
import leaf.cosmere.allomancy.common.network.packets.PlayerShootProjectileMessage;
import leaf.cosmere.common.network.BasePacketHandler;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.common.network.packets.SyncPushPullMessage;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class AllomancyPacketHandler extends BasePacketHandler
{
	@Override
	protected String getProtocolVersion()
	{
		return Allomancy.instance.versionNumber.toString();
	}

	@Override
	public void initialize(PayloadRegistrar registrar)
	{
		registrar.playToServer(
				PlayerShootProjectileMessage.TYPE,
				PlayerShootProjectileMessage.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				SyncPushPullMessage.TYPE,
				SyncPushPullMessage.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				EntityAllomancyActivateMessage.TYPE,
				EntityAllomancyActivateMessage.STREAM_CODEC,
				ICosmerePacket::handle);
	}
}
