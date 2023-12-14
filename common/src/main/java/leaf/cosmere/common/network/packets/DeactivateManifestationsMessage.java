/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.network.packets;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import static leaf.cosmere.api.Constants.Translations.POWER_INACTIVE;

public class DeactivateManifestationsMessage implements ICosmerePacket
{

	public DeactivateManifestationsMessage()
	{
	}

	public DeactivateManifestationsMessage(FriendlyByteBuf buffer)
	{
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			MutableComponent manifestationText = POWER_INACTIVE;

			cap.deactivateManifestations();

			sender.sendSystemMessage(manifestationText);
			cap.syncToClients(null);
		}));
		context.setPacketHandled(true);
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{

	}

}
