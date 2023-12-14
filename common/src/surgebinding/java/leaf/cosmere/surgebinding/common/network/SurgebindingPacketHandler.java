/*
 * File updated ~ 10 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.network;

import leaf.cosmere.common.network.BasePacketHandler;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.network.packets.SummonShardblade;
import net.minecraftforge.network.simple.SimpleChannel;

public class SurgebindingPacketHandler extends BasePacketHandler
{
	private final SimpleChannel NETWORK_CHANNEL = createChannel(Surgebinding.rl(Surgebinding.MODID), Surgebinding.instance.versionNumber);

	@Override
	protected SimpleChannel getChannel()
	{
		return NETWORK_CHANNEL;
	}

	@Override
	public void initialize()
	{
		registerClientToServer(SummonShardblade.class, SummonShardblade::new);
	}


}
