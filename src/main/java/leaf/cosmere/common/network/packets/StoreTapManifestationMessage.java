package leaf.cosmere.common.network.packets;

import leaf.cosmere.api.IHasManifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.common.registry.ManifestationRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
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

	public StoreTapManifestationMessage(Manifestation manifestation, double manifestationStrength, int curioSlot, boolean isStore)
	{
		this.manifestation = manifestation;
		this.manifestationStrength = manifestationStrength;
		this.curioSlot = curioSlot;
		this.isStore = isStore;
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer sender = context.getSender();
		context.enqueueWork(() ->
		{
			if (sender == null) return;
			SpiritwebCapability.get(sender).ifPresent((cap) ->
			{
				// Storing
				if (isStore)
				{
					LazyOptional<ICuriosItemHandler> curiosItemHandler = CuriosApi.getCuriosInventory(sender);
					if (curiosItemHandler.resolve().isPresent())
					{
						ICuriosItemHandler itemHandler = curiosItemHandler.resolve().get();
						if (itemHandler.getEquippedCurios().getStackInSlot(curioSlot).getItem() instanceof IHasManifestations item)
						{
							cap.removeManifestation(manifestation);
							item.addManifestation(itemHandler.getEquippedCurios().getStackInSlot(curioSlot), manifestation, (int) manifestationStrength);
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
						if (itemHandler.getEquippedCurios().getStackInSlot(curioSlot).getItem() instanceof IHasManifestations item)
						{
							cap.giveManifestation(manifestation, (int) manifestationStrength);
							item.removeManifestation(itemHandler.getEquippedCurios().getStackInSlot(curioSlot), manifestation);
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
	}

	public static StoreTapManifestationMessage decode(FriendlyByteBuf buf)
	{
		String manifestation = buf.readUtf();
		double manifestationStrength = buf.readDouble();
		int curioSlot = buf.readInt();
		boolean isStore = buf.readBoolean();
		return new StoreTapManifestationMessage(ManifestationRegistry.fromID(manifestation), manifestationStrength, curioSlot, isStore);
	}
}
