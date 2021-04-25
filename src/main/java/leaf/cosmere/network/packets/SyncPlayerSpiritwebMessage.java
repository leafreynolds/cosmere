/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPlayerSpiritwebMessage
{

    public int id;
    public CompoundNBT data;

    public SyncPlayerSpiritwebMessage(int id, CompoundNBT data)
    {
        this.id = id;
        this.data = data;
    }

    public static void encode(SyncPlayerSpiritwebMessage mes, PacketBuffer buf)
    {
        buf.writeInt(mes.id);
        buf.writeCompoundTag(mes.data);
    }

    public static SyncPlayerSpiritwebMessage decode(PacketBuffer buf)
    {
        return new SyncPlayerSpiritwebMessage(buf.readInt(), buf.readCompoundTag());
    }

    public static void handle(SyncPlayerSpiritwebMessage mes, Supplier<NetworkEvent.Context> cont)
    {
        cont.get().enqueueWork(() ->
        {
            Entity result = Minecraft.getInstance().world.getEntityByID(mes.id);
            if (result != null)
            {
                result.getCapability(Capabilities.SPIRITWEB_CAPABILITY).ifPresent(cap -> cap.deserializeNBT(mes.data));
            }
        });
        cont.get().setPacketHandled(true);
    }

}
