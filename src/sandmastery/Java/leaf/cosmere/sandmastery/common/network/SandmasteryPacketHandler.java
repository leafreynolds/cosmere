/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.network;

import leaf.cosmere.sandmastery.common.network.packets.PlayerShootSandProjectileMessage;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.BasePacketHandler;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.network.packets.SyncMasteryBindsMessage;
import net.minecraftforge.network.simple.SimpleChannel;

public class SandmasteryPacketHandler extends BasePacketHandler
{
    private final SimpleChannel NETWORK_CHANNEL = createChannel(Cosmere.rl(Sandmastery.MODID), Sandmastery.instance.versionNumber);

    @Override
    protected SimpleChannel getChannel()
    {
        return NETWORK_CHANNEL;
    }

    @Override
    public void initialize()
    {
        registerClientToServer(PlayerShootSandProjectileMessage.class, PlayerShootSandProjectileMessage::decode);
        registerClientToServer(SyncMasteryBindsMessage.class, SyncMasteryBindsMessage::decode);

    }


}
