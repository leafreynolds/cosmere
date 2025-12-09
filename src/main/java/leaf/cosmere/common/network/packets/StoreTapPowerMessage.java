package leaf.cosmere.common.network.packets;

import leaf.cosmere.common.charge.IHoldsPowers;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.common.util.CosmereAttributeUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class StoreTapPowerMessage implements ICosmerePacket
{
	Attribute attribute;
	double attributeStrength;
	int curioSlot;
	boolean isStore;
	byte attributeSlot;

	public StoreTapPowerMessage(Attribute attribute, double attributeStrength, int curioSlot, boolean isStore, byte attributeSlot)
	{
		this.attribute = attribute;
		this.attributeStrength = attributeStrength;
		this.curioSlot = curioSlot;
		this.isStore = isStore;
		this.attributeSlot = attributeSlot;
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		context.enqueueWork(() ->
		{
			if (sender == null)
			{
				return;
			}
			SpiritwebCapability.get(sender).ifPresent((cap) ->
			{
				// Storing
				if (isStore)
				{
					LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(sender);
					if (curiosItemHandler.resolve().isPresent())
					{
						ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
						ItemStack itemStack = itemHandler.getEquippedCurios().getStackInSlot(curioSlot);
						if (itemHandler.getEquippedCurios().getStackInSlot(curioSlot).getItem() instanceof IHoldsPowers item)
						{
							if(item.trySetAttunedPlayer(itemStack, sender))
							{
								CosmereAttributeUtils.removeBaseAttribute(sender, attribute);
								item.addPower(itemHandler.getEquippedCurios().getStackInSlot(curioSlot), attribute, (int) attributeStrength, attributeSlot);
							}
						}
					}
				}
				// Tapping
				else
				{
					LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(sender);
					if (curiosItemHandler.resolve().isPresent())
					{
						ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
						ItemStack itemStack = itemHandler.getEquippedCurios().getStackInSlot(curioSlot);
						if (itemHandler.getEquippedCurios().getStackInSlot(curioSlot).getItem() instanceof IHoldsPowers item)
						{
							if(item.getPlayerIsAttuned(itemStack, sender))
							{
								CosmereAttributeUtils.grantBaseAttribute(sender, (RangedAttribute) attribute, (int) attributeStrength);
								item.removePower(itemHandler.getEquippedCurios().getStackInSlot(curioSlot), attribute);
							}
						}
					}
				}
				cap.syncToClients(sender);
			});
		});
		context.setPacketHandled(true);
	}


	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUtf(this.attribute.getDescriptionId());
		buf.writeDouble(this.attributeStrength);
		buf.writeInt(this.curioSlot);
		buf.writeBoolean(this.isStore);
		buf.writeByte(this.attributeSlot);
	}

	public static StoreTapPowerMessage decode(FriendlyByteBuf buf)
	{
		String attributeId = buf.readUtf();
		double attributeStrength = buf.readDouble();
		int curioSlot = buf.readInt();
		boolean isStore = buf.readBoolean();
		byte attributeSlot = buf.readByte();

		return new StoreTapPowerMessage(CosmereAttributeUtils.getAttributeByDescriptionId(attributeId), attributeStrength, curioSlot, isStore, attributeSlot);
	}
}
