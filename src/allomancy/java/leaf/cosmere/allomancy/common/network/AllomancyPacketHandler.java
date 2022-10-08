/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.network;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.network.packets.PlayerShootProjectileMessage;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.BasePacketHandler;
import leaf.cosmere.common.network.packets.SyncPushPullMessage;
import net.minecraftforge.network.simple.SimpleChannel;

public class AllomancyPacketHandler extends BasePacketHandler
{
	private final SimpleChannel NETWORK_CHANNEL = createChannel(Cosmere.rl(Allomancy.MODID), Allomancy.instance.versionNumber);

	@Override
	protected SimpleChannel getChannel()
	{
		return NETWORK_CHANNEL;
	}

	@Override
	public void initialize()
	{
		registerClientToServer(PlayerShootProjectileMessage.class, PlayerShootProjectileMessage::decode);
		registerClientToServer(SyncPushPullMessage.class, SyncPushPullMessage::decode);

	}


}
