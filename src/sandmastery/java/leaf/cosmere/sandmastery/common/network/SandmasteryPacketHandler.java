/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.network;

import leaf.cosmere.common.network.BasePacketHandler;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.network.packets.SyncMasteryBindsMessage;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class SandmasteryPacketHandler extends BasePacketHandler
{
	@Override
	protected String getProtocolVersion()
	{
		return Sandmastery.instance.versionNumber.toString();
	}

	@Override
	public void initialize(PayloadRegistrar registrar)
	{
		registrar.playToServer(
				SyncMasteryBindsMessage.TYPE,
				SyncMasteryBindsMessage.STREAM_CODEC,
				ICosmerePacket::handle);
	}
}
