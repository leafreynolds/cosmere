/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkEvent;

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
        ServerPlayerEntity sender = context.getSender();
        MinecraftServer server = sender.getServer();
        server.submitAsync(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
        {
            cap.setSelectedManifestation(message.manifestation);
            cap.syncToClients(null);
        }));
        context.setPacketHandled(true);
    }


    public static void encode(SetSelectedManifestationMessage mes, PacketBuffer buf)
    {
        String namespace = mes.manifestation.getRegistryName().toString();
        buf.writeUtf(namespace);
    }

    public static SetSelectedManifestationMessage decode(PacketBuffer buf)
    {
        String location = buf.readUtf();
        return new SetSelectedManifestationMessage(ManifestationRegistry.fromID(location));
    }

}
