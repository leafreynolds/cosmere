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

public class SetSelectedManifestationMessage
{
	AManifestation manifestation;

	public SetSelectedManifestationMessage(AManifestation manifestation)
	{
		this.manifestation = manifestation;
	}

	public static void handle(SetSelectedManifestationMessage message, Supplier<NetworkEvent.Context> ctx)
	{
		NetworkEvent.Context context = ctx.get();
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			cap.setSelectedManifestation(message.manifestation);
			cap.syncToClients(null);
		}));
		context.setPacketHandled(true);
	}


	public static void encode(SetSelectedManifestationMessage mes, FriendlyByteBuf buf)
	{
		String namespace = mes.manifestation.getResourceLocation().toString();
		buf.writeUtf(namespace);
	}

	public static SetSelectedManifestationMessage decode(FriendlyByteBuf buf)
	{
		String location = buf.readUtf();
		return new SetSelectedManifestationMessage(ManifestationRegistry.fromID(location));
	}

}
