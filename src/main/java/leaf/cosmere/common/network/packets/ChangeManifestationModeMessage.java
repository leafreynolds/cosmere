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

public class ChangeManifestationModeMessage implements ICosmerePacket
{
	Manifestation manifestation;
	int modifier;

	public ChangeManifestationModeMessage(Manifestation manifestation, int dir)
	{
		this.manifestation = manifestation;
		this.modifier = dir;
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((data) ->
		{
			int finalModifier = manifestation.getModeModifier(data, modifier);
			if (finalModifier == 1)
			{
				data.nextMode(manifestation);
			}
			else if (finalModifier == -1)
			{
				data.previousMode(manifestation);
			}
			else if (finalModifier != 0)
			{
				int newMode = finalModifier + manifestation.getMode(data);
				data.setMode(manifestation, newMode);
			}

			data.syncToClients(null);
		}));
		context.setPacketHandled(true);
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		String namespace = manifestation.getRegistryName().toString();
		buf.writeUtf(namespace);
		buf.writeInt(modifier);
	}

	public static ChangeManifestationModeMessage decode(FriendlyByteBuf buf)
	{
		String location = buf.readUtf();
		final int dir = buf.readInt();
		return new ChangeManifestationModeMessage(ManifestationRegistry.fromID(location), dir);
	}

}
