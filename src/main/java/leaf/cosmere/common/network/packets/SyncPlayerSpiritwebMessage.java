/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.network.packets;

import leaf.cosmere.client.gui.ISyncSpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class SyncPlayerSpiritwebMessage implements ICosmerePacket
{
	public int entityID;
	public CompoundTag entityNBT;

	public SyncPlayerSpiritwebMessage(int entityID, CompoundTag entityNBT)
	{
		this.entityID = entityID;
		this.entityNBT = entityNBT;
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeInt(entityID);
		buf.writeNbt(entityNBT);
	}

	public static SyncPlayerSpiritwebMessage decode(FriendlyByteBuf buf)
	{
		return new SyncPlayerSpiritwebMessage(buf.readInt(), buf.readNbt());
	}

	@Override
	public void handle(NetworkEvent.Context cont)
	{
		cont.enqueueWork(() ->
		{
			Minecraft mc = Minecraft.getInstance();
			if (mc.level == null)
			{
				return;
			}

			Entity result = mc.level.getEntity(entityID);
			if (!(result instanceof LivingEntity living))
			{
				return;
			}

			if (living != mc.player)
			{
				SpiritwebCapability.get(living).ifPresent(c ->
				{
					c.deserializeNBT(entityNBT);
					c.getLiving().refreshDimensions();
				});
				return;
			}

			SpiritwebCapability.get(living).ifPresent(c ->
			{
				c.deserializeNBT(entityNBT);
				c.getLiving().refreshDimensions();

				if (mc.screen instanceof ISyncSpiritweb spiritMenu)
				{
					// optional: check it’s the same capability instance
					if (spiritMenu.getSpiritweb() == c)
					{
						spiritMenu.onSpiritwebUpdated((SpiritwebCapability) c);
					}
				}
			});
		});

		cont.setPacketHandled(true);
	}

}
