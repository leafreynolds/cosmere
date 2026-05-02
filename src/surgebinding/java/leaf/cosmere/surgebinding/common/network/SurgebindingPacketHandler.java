/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.surgebinding.common.network;

import leaf.cosmere.common.network.BasePacketHandler;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.network.packets.DispatchStormlight;
import leaf.cosmere.surgebinding.common.network.packets.RequestStormlight;
import leaf.cosmere.surgebinding.common.network.packets.SummonShardblade;
import leaf.cosmere.surgebinding.common.network.packets.SyncShardCapMessage;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class SurgebindingPacketHandler extends BasePacketHandler
{
	@Override
	protected String getProtocolVersion()
	{
		return Surgebinding.instance.versionNumber.toString();
	}

	@Override
	public void initialize(PayloadRegistrar registrar)
	{
		registrar.playToClient(
				SyncShardCapMessage.TYPE,
				SyncShardCapMessage.STREAM_CODEC,
				ICosmerePacket::handle);

		registrar.playToServer(
				SummonShardblade.TYPE,
				SummonShardblade.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				DispatchStormlight.TYPE,
				DispatchStormlight.STREAM_CODEC,
				ICosmerePacket::handle);
		registrar.playToServer(
				RequestStormlight.TYPE,
				RequestStormlight.STREAM_CODEC,
				ICosmerePacket::handle);
	}
}
