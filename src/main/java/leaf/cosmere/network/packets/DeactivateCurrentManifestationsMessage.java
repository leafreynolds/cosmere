/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static leaf.cosmere.constants.Constants.Translations.POWER_INACTIVE;

public class DeactivateCurrentManifestationsMessage
{

    public DeactivateCurrentManifestationsMessage()
    {
    }

    public DeactivateCurrentManifestationsMessage(PacketBuffer buffer)
    {
    }

    public static void handle(DeactivateCurrentManifestationsMessage message, Supplier<NetworkEvent.Context> ctx)
    {
        NetworkEvent.Context context = ctx.get();
        ServerPlayerEntity sender = context.getSender();
        MinecraftServer server = sender.getServer();
        server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
        {
            TextComponent manifestationText= POWER_INACTIVE;

            cap.deactivateManifestations();

            sender.sendMessage(manifestationText, Util.NIL_UUID);
            cap.syncToClients(null);
        }));
        context.setPacketHandled(true);
    }

    public void encode(PacketBuffer buf)
    {

    }

}
