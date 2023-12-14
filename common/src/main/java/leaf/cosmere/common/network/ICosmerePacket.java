package leaf.cosmere.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface ICosmerePacket
{
	void handle(NetworkEvent.Context context);

	void encode(FriendlyByteBuf buffer);

	static <PACKET extends ICosmerePacket> void handle(PACKET message, Supplier<NetworkEvent.Context> ctx)
	{
		if (message != null)
		{
			NetworkEvent.Context context = ctx.get();
			context.enqueueWork(() -> message.handle(context));
			context.setPacketHandled(true);
		}
	}
}