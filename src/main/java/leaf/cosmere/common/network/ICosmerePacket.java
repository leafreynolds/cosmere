package leaf.cosmere.common.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ICosmerePacket extends CustomPacketPayload
{
	void handle(IPayloadContext context);
}
