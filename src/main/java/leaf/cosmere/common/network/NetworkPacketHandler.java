/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Special thank you to the New Tardis Mod team.
 * That mod taught me how to do proper syncing between server and client.
 * https://tardis-mod.com/books/home/page/links#bkmrk-source
 */

package leaf.cosmere.common.network;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.packets.*;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkPacketHandler extends BasePacketHandler
{
	@Override
	protected String getProtocolVersion()
	{
		return Cosmere.instance.versionNumber.toString();
	}

	@Override
	public void initialize(PayloadRegistrar registrar)
	{
		//server to client
		registrar.playToClient(
				SyncPlayerSpiritwebMessage.TYPE,
				SyncPlayerSpiritwebMessage.STREAM_CODEC,
				ICosmerePacket::handle);

		//client to server
		registrar.playToServer(
				DeactivateManifestationsMessage.TYPE,
				DeactivateManifestationsMessage.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				ChangeManifestationModeMessage.TYPE,
				ChangeManifestationModeMessage.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				ChangeSelectedManifestationMessage.TYPE,
				ChangeSelectedManifestationMessage.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				SetSelectedManifestationMessage.TYPE,
				SetSelectedManifestationMessage.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				StoreTapPowerMessage.TYPE,
				StoreTapPowerMessage.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				SavePowerStateMessage.TYPE,
				SavePowerStateMessage.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				TogglePowerStateMessage.TYPE,
				TogglePowerStateMessage.STREAM_CODEC,
				ICosmerePacket::handle);
	}
}
