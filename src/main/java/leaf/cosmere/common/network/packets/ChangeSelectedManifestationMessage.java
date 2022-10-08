/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.network.packets;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ChangeSelectedManifestationMessage implements ICosmerePacket
{
	int dir;

	public ChangeSelectedManifestationMessage(int dir)
	{
		this.dir = dir;
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			cap.changeManifestation(dir);
			cap.syncToClients(null);
		}));
		context.setPacketHandled(true);
	}


	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeInt(dir);
	}

	public static ChangeSelectedManifestationMessage decode(FriendlyByteBuf buf)
	{
		return new ChangeSelectedManifestationMessage(buf.readInt());
	}

}
