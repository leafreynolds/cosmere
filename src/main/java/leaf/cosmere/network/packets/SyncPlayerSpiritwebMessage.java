/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.network.packets;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncPlayerSpiritwebMessage
{
	public int entityID;
	public CompoundTag entityNBT;

	public SyncPlayerSpiritwebMessage(int entityID, CompoundTag entityNBT)
	{
		this.entityID = entityID;
		this.entityNBT = entityNBT;
	}

	public static void encode(SyncPlayerSpiritwebMessage mes, FriendlyByteBuf buf)
	{
		buf.writeInt(mes.entityID);
		buf.writeNbt(mes.entityNBT);
	}

	public static SyncPlayerSpiritwebMessage decode(FriendlyByteBuf buf)
	{
		return new SyncPlayerSpiritwebMessage(buf.readInt(), buf.readNbt());
	}

	public static void handle(SyncPlayerSpiritwebMessage mes, Supplier<NetworkEvent.Context> cont)
	{
		cont.get().enqueueWork(() ->
		{
			Entity result = Minecraft.getInstance().level.getEntity(mes.entityID);
			if (result != null)
			{
				SpiritwebCapability.get((LivingEntity) result).ifPresent((c) -> c.deserializeNBT(mes.entityNBT));
			}
		});
		cont.get().setPacketHandled(true);
	}

}
