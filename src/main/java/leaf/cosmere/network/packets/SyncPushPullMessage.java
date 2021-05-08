/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.helpers.CodecHelper;
import leaf.cosmere.helpers.LogHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SyncPushPullMessage
{
    public CompoundNBT data;

    public SyncPushPullMessage(CompoundNBT data)
    {
        this.data = data;
    }

    public static void encode(SyncPushPullMessage mes, PacketBuffer buf)
    {
        buf.writeCompoundTag(mes.data);
    }

    public static SyncPushPullMessage decode(PacketBuffer buf)
    {
        return new SyncPushPullMessage(buf.readCompoundTag());
    }

    public static void handle(SyncPushPullMessage mes, Supplier<NetworkEvent.Context> cont)
    {

        NetworkEvent.Context context = cont.get();
        ServerPlayerEntity sender = context.getSender();
        MinecraftServer server = sender.getServer();
        server.deferTask(() -> SpiritwebCapability.get(sender).ifPresent((cap) ->
        {
            SpiritwebCapability spiritweb = (SpiritwebCapability) cap;

            final String pushBlocks = "pushBlocks";
            final String pullBlocks = "pullBlocks";

            boolean isPushMessage = mes.data.contains(pushBlocks);
            String target = isPushMessage ? pushBlocks : pullBlocks;

            CodecHelper.BlockPosListCodec.decode(NBTDynamicOps.INSTANCE,
                    mes.data.getList(target, Constants.NBT.TAG_INT_ARRAY))
                    .resultOrPartial(LogHelper.LOGGER::error)
                    .ifPresent(listINBTPair ->
                    {
                        List<BlockPos> messageBlocks = listINBTPair.getFirst();

                        if (isPushMessage)
                        {
                            spiritweb.pushBlocks.clear();
                            spiritweb.pushBlocks.addAll(messageBlocks);
                        }
                        else
                        {
                            spiritweb.pullBlocks.clear();
                            spiritweb.pullBlocks.addAll(messageBlocks);
                        }
                    });


            LogHelper.info("pullBlocks updated");

        }));
        cont.get().setPacketHandled(true);
    }

}
