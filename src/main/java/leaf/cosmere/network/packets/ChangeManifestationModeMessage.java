/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeManifestationModeMessage
{
	AManifestation manifestation;
	int dir;

	public ChangeManifestationModeMessage(AManifestation aManifestation, int dir)
	{
		this.manifestation = aManifestation;
		this.dir = dir;
	}

	public static void handle(ChangeManifestationModeMessage message, Supplier<NetworkEvent.Context> ctx)
	{
		NetworkEvent.Context context = ctx.get();
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((data) ->
		{

			if (message.dir == 1)
			{
				data.nextMode(message.manifestation);
			}
			else if (message.dir == -1)
			{
				data.previousMode(message.manifestation);
			}
			else if (message.dir != 0)
			{
				int newMode = message.dir + message.manifestation.getMode(data);
				data.setMode(message.manifestation, newMode);
			}

			data.syncToClients(null);
		}));
		context.setPacketHandled(true);
	}

	public static void encode(ChangeManifestationModeMessage mes, FriendlyByteBuf buf)
	{
		String namespace = mes.manifestation.getResourceLocation().toString();
		buf.writeUtf(namespace);
		buf.writeInt(mes.dir);
	}

	public static ChangeManifestationModeMessage decode(FriendlyByteBuf buf)
	{
		String location = buf.readUtf();
		final int dir = buf.readInt();
		return new ChangeManifestationModeMessage(ManifestationRegistry.fromID(location), dir);
	}

}
