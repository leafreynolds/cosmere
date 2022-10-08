/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.network.packets;

import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.common.registry.ManifestationRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SetSelectedManifestationMessage implements ICosmerePacket
{
	Manifestation manifestation;

	public SetSelectedManifestationMessage(Manifestation manifestation)
	{
		this.manifestation = manifestation;
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			cap.setSelectedManifestation(manifestation);
			cap.syncToClients(null);
		}));
		context.setPacketHandled(true);
	}


	@Override
	public void encode(FriendlyByteBuf buf)
	{
		String namespace = manifestation.getRegistryName().toString();
		buf.writeUtf(namespace);
	}

	public static SetSelectedManifestationMessage decode(FriendlyByteBuf buf)
	{
		String location = buf.readUtf();
		return new SetSelectedManifestationMessage(ManifestationRegistry.fromID(location));
	}

}
