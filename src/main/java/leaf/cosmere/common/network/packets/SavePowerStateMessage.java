package leaf.cosmere.common.network.packets;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SavePowerStateMessage implements ICosmerePacket
{
	private final int powerState;

	public SavePowerStateMessage(int powerState)
	{
		this.powerState = powerState;
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((data) ->
		{
			data.saveNewState(powerState);
		}));
		context.setPacketHandled(true);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(powerState);
	}

	public static SavePowerStateMessage decode(FriendlyByteBuf buf)
	{
		int powerState = buf.readInt();
		return new SavePowerStateMessage(powerState);
	}
}
