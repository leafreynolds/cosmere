/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

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
        ServerPlayerEntity sender = context.getSender();
        MinecraftServer server = sender.getServer();
        server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
        {
            TextComponent manifestationText;
            String manifestation;

            manifestation = cap.changeManifestation(message.dir);

            manifestationText = TextHelper.createTranslatedText(Constants.Strings.POWER_SET_SUCCESS, TextHelper.createTranslatedText(manifestation));

            sender.sendMessage(manifestationText, Util.NIL_UUID);
            cap.syncToClients(null);
        }));
        context.setPacketHandled(true);
    }


    public static void encode(ChangeSelectedManifestationMessage mes, PacketBuffer buf)
    {
        buf.writeInt(mes.dir);
    }

    public static ChangeSelectedManifestationMessage decode(PacketBuffer buf)
    {
        return new ChangeSelectedManifestationMessage(buf.readInt());
    }

}
