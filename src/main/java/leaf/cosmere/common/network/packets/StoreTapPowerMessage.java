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
	int attributeStrength;
	int itemSlot;
    int attributeSlot;
	boolean isStore;
    boolean isCurio;

	public StoreTapPowerMessage(Attribute attribute, int attributeStrength, int itemSlot, int attributeSlot, boolean isStore, boolean isCurio)
	{
		this.attribute = attribute;
		this.attributeStrength = attributeStrength;
        this.itemSlot = itemSlot;
        this.attributeSlot = attributeSlot;
		this.isStore = isStore;
		this.isCurio = isCurio;
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
                    ItemStack itemStack = CosmereAttributeUtils.getPowerItem(sender, itemSlot, isCurio);
                    if (itemStack.getItem() instanceof IHoldsPowers item)
                    {
                        if(item.trySetAttunedPlayer(itemStack, sender))
                        {
                            CosmereAttributeUtils.removeBaseAttribute(sender, attribute);
                            item.addPower(itemStack, attribute, attributeStrength, attributeSlot);
                        }
                    }
				}
				// Tapping
				else
				{
                    ItemStack itemStack = CosmereAttributeUtils.getPowerItem(sender, itemSlot, isCurio);
                    if (itemStack.getItem() instanceof IHoldsPowers item)
                    {
                        if(item.getPlayerIsAttuned(itemStack, sender))
                        {
                            CosmereAttributeUtils.grantBaseAttribute(sender, (RangedAttribute) attribute, attributeStrength);
                            item.removePower(itemStack, attribute);
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
		buf.writeInt(this.attributeStrength);
		buf.writeInt(this.itemSlot);
        buf.writeInt(this.attributeSlot);
		buf.writeBoolean(this.isStore);
        buf.writeBoolean(this.isCurio);
	}

	public static StoreTapPowerMessage decode(FriendlyByteBuf buf)
	{
		String attributeId = buf.readUtf();
		int attributeStrength = buf.readInt();
		int itemSlot = buf.readInt();
        int attributeSlot = buf.readInt();
		boolean isStore = buf.readBoolean();
        boolean isCurio = buf.readBoolean();

		return new StoreTapPowerMessage(CosmereAttributeUtils.getAttributeByDescriptionId(attributeId), attributeStrength, itemSlot, attributeSlot, isStore, isCurio);
	}
}
