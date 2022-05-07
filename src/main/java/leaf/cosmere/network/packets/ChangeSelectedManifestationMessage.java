/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeSelectedManifestationMessage
{
	int dir;

	public ChangeSelectedManifestationMessage(int dir)
	{
		this.dir = dir;
	}

	public static void handle(ChangeSelectedManifestationMessage message, Supplier<NetworkEvent.Context> ctx)
	{
		NetworkEvent.Context context = ctx.get();
		ServerPlayer sender = context.getSender();
		MinecraftServer server = sender.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
		{
			BaseComponent manifestationText;
			String manifestation;

			manifestation = cap.changeManifestation(message.dir);

			manifestationText = TextHelper.createTranslatedText(Constants.Strings.POWER_SET_SUCCESS, TextHelper.createTranslatedText(manifestation));

			sender.sendMessage(manifestationText, Util.NIL_UUID);
			cap.syncToClients(null);
		}));
		context.setPacketHandled(true);
	}


	public static void encode(ChangeSelectedManifestationMessage mes, FriendlyByteBuf buf)
	{
		buf.writeInt(mes.dir);
	}

	public static ChangeSelectedManifestationMessage decode(FriendlyByteBuf buf)
	{
		return new ChangeSelectedManifestationMessage(buf.readInt());
	}

}
