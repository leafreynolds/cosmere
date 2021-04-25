/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the New Tardis Mod team.
 * That mod taught me how to do proper syncing between server and client.
 * https://tardis-mod.com/books/home/page/links#bkmrk-source
 */

package leaf.cosmere.network;

import leaf.cosmere.network.packets.*;
import leaf.cosmere.Cosmere;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Network
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Cosmere.MODID, Cosmere.MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);


    public static void init()
    {
        int id = 0;
        NETWORK_CHANNEL.registerMessage(id++, SyncPlayerSpiritwebMessage.class, SyncPlayerSpiritwebMessage::encode, SyncPlayerSpiritwebMessage::decode, SyncPlayerSpiritwebMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, DeactivateCurrentManifestationsMessage.class, DeactivateCurrentManifestationsMessage::encode, DeactivateCurrentManifestationsMessage::new, DeactivateCurrentManifestationsMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, ChangeManifestationModeMessage.class, ChangeManifestationModeMessage::encode, ChangeManifestationModeMessage::decode, ChangeManifestationModeMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, DeactivateAllManifestationsMessage.class, DeactivateAllManifestationsMessage::encode, DeactivateAllManifestationsMessage::new, DeactivateAllManifestationsMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, ChangeSelectedManifestationMessage.class, ChangeSelectedManifestationMessage::encode, ChangeSelectedManifestationMessage::decode, ChangeSelectedManifestationMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, SetSelectedManifestationMessage.class, SetSelectedManifestationMessage::encode, SetSelectedManifestationMessage::decode, SetSelectedManifestationMessage::handle);

    }


    /**
     * Sends a packet to the server.<br>
     * Must be called Client side.
     */
    public static void sendToServer(Object msg)
    {
        NETWORK_CHANNEL.sendToServer(msg);
    }

    /**
     * Send a packet to a specific player.<br>
     * Must be called Server side.
     */
    public static void sendTo(Object msg, ServerPlayerEntity player)
    {
        if (!(player instanceof FakePlayer))
        {
            NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
        }
    }

    public static void sendPacketToAll(Object packet)
    {
        NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static SUpdateTileEntityPacket createTEUpdatePacket(TileEntity tile)
    {
        return new SUpdateTileEntityPacket(tile.getPos(), -1, tile.getUpdateTag());
    }

    public static void sendToAllAround(Object mes, RegistryKey<World> dim, BlockPos pos, int radius)
    {
        NETWORK_CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), radius, dim)), mes);
    }

    public static void sendToAllInWorld(Object mes, ServerWorld world)
    {
        NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(world::getDimensionKey), mes);
    }

    public static void sendToTrackingTE(Object mes, TileEntity te)
    {
        if (te != null && !te.getWorld().isRemote)
        {
            NETWORK_CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> te.getWorld().getChunkAt(te.getPos())), mes);
        }
    }
}
