package leaf.cosmere.metallurgy.common.network;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.BasePacketHandler;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.network.packets.CutIngotPacket;
import net.minecraftforge.network.simple.SimpleChannel;

public class MetallurgyPacketHandler extends BasePacketHandler {
    private final SimpleChannel NETWORK_CHANNEL = createChannel(Cosmere.rl(Metallurgy.MODID), Metallurgy.instance.versionNumber);

    @Override
    protected SimpleChannel getChannel() {
        return NETWORK_CHANNEL;
    }

    @Override
    public void initialize() {
        registerClientToServer(CutIngotPacket.class, CutIngotPacket::decode);
    }
}
