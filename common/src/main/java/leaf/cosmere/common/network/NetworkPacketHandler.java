/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the New Tardis Mod team.
 * That mod taught me how to do proper syncing between server and client.
 * https://tardis-mod.com/books/home/page/links#bkmrk-source
 */

package leaf.cosmere.common.network;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.packets.*;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkPacketHandler extends BasePacketHandler
{
	private final SimpleChannel NETWORK_CHANNEL = createChannel(Cosmere.rl(Cosmere.MODID), Cosmere.instance.versionNumber);

	@Override
	protected SimpleChannel getChannel()
	{
		return NETWORK_CHANNEL;
	}

	@Override
	public void initialize()
	{
		//server to client
		registerServerToClient(SyncPlayerSpiritwebMessage.class, SyncPlayerSpiritwebMessage::decode);

		//client to server
		registerClientToServer(DeactivateManifestationsMessage.class, DeactivateManifestationsMessage::new);
		registerClientToServer(ChangeManifestationModeMessage.class, ChangeManifestationModeMessage::decode);
		registerClientToServer(ChangeSelectedManifestationMessage.class, ChangeSelectedManifestationMessage::decode);
		registerClientToServer(SetSelectedManifestationMessage.class, SetSelectedManifestationMessage::decode);

	}


}
