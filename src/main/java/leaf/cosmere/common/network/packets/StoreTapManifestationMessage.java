package leaf.cosmere.common.network.packets;

import leaf.cosmere.common.charge.IHasManifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.common.registry.ManifestationRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class StoreTapManifestationMessage implements ICosmerePacket
{
	Manifestation manifestation;
	double manifestationStrength;
	int curioSlot;
	boolean isStore;
	byte manifestationSlot;

	public StoreTapManifestationMessage(Manifestation manifestation, double manifestationStrength, int curioSlot, boolean isStore, byte manifestationSlot)
	{
		this.manifestation = manifestation;
		this.manifestationStrength = manifestationStrength;
		this.curioSlot = curioSlot;
		this.isStore = isStore;
		this.manifestationSlot = manifestationSlot;
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
						if (itemHandler.getEquippedCurios().getStackInSlot(curioSlot).getItem() instanceof IHasManifestations item)
						{
							if(item.trySetAttunedPlayer(itemStack, sender))
							{
								cap.removeManifestation(manifestation);
								item.addManifestation(itemHandler.getEquippedCurios().getStackInSlot(curioSlot), manifestation, (int) manifestationStrength, manifestationSlot);
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
						if (itemHandler.getEquippedCurios().getStackInSlot(curioSlot).getItem() instanceof IHasManifestations item)
						{
							int currentStrength = 0;
							if (!(manifestation.getAttribute() instanceof RangedAttribute attribute))
							{
								return;
							}
							AttributeInstance attributeInstance = sender.getAttribute(attribute);
							if (attributeInstance != null)
							{
								currentStrength = (int) attributeInstance.getBaseValue();
							}

							// Let's ensure not to exceed the base value if it's out of range,
							// even if it will get sanitized
							int newStrength = (int) manifestationStrength + currentStrength;
							if (newStrength < attribute.getMinValue())
							{
								newStrength = (int) attribute.getMinValue();
							}
							else if (newStrength > attribute.getMaxValue())
							{
								newStrength = (int) attribute.getMaxValue();
							}

							if(item.getPlayerIsAttuned(itemStack, sender))
							{
								cap.giveManifestation(manifestation, (int) newStrength);
								item.removeManifestation(itemHandler.getEquippedCurios().getStackInSlot(curioSlot), manifestation);
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
		buf.writeUtf(this.manifestation.getRegistryName().toString());
		buf.writeDouble(this.manifestationStrength);
		buf.writeInt(this.curioSlot);
		buf.writeBoolean(this.isStore);
		buf.writeByte(this.manifestationSlot);
	}

	public static StoreTapManifestationMessage decode(FriendlyByteBuf buf)
	{
		String manifestation = buf.readUtf();
		double manifestationStrength = buf.readDouble();
		int curioSlot = buf.readInt();
		boolean isStore = buf.readBoolean();
		byte manifestationSlot = buf.readByte();
		return new StoreTapManifestationMessage(ManifestationRegistry.fromID(manifestation), manifestationStrength, curioSlot, isStore, manifestationSlot);
	}
}
