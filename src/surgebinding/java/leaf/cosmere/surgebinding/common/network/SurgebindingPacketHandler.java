package leaf.cosmere.surgebinding.common.network;

import leaf.cosmere.common.network.BasePacketHandler;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.network.packets.DispatchStormlight;
import leaf.cosmere.surgebinding.common.network.packets.RequestStormlight;
import leaf.cosmere.surgebinding.common.network.packets.SummonShardblade;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
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
		registrar.playToServer(SummonShardblade.TYPE, SummonShardblade.STREAM_CODEC, SummonShardblade::handle);
		registrar.playToServer(DispatchStormlight.TYPE, DispatchStormlight.STREAM_CODEC, DispatchStormlight::handle);
		registrar.playToServer(RequestStormlight.TYPE, RequestStormlight.STREAM_CODEC, RequestStormlight::handle);
	}
}
