/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static leaf.cosmere.constants.Constants.Translations.POWER_INACTIVE;

public class DeactivateCurrentManifestationsMessage
{

	public DeactivateCurrentManifestationsMessage()
	{
	}

	public DeactivateCurrentManifestationsMessage(FriendlyByteBuf buffer)
	{
	}

	public static void handle(DeactivateCurrentManifestationsMessage message, Supplier<NetworkEvent.Context> ctx)
	{
		NetworkEvent.Context context = ctx.get();
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			BaseComponent manifestationText = POWER_INACTIVE;

			cap.deactivateManifestations();

			sender.sendMessage(manifestationText, Util.NIL_UUID);
			cap.syncToClients(null);
		}));
		context.setPacketHandled(true);
	}

	public void encode(FriendlyByteBuf buf)
	{

	}

}
