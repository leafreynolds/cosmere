/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;

import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.manifestation.AManifestation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler
{
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        Capability.IStorage<ISpiritweb> storage = SpiritwebCapability.CAPABILITY.getStorage();
        event.getOriginal().revive();
        SpiritwebCapability.get(event.getOriginal()).ifPresent((oldSpiritWeb) -> SpiritwebCapability.get(event.getPlayer()).ifPresent((newSpiritWeb) ->
        {
            //clear out the attributes that were placed on the newly cloned player at creation
            // and make sure that they then match what the old player entity had.
            newSpiritWeb.clearManifestations();
            for (AManifestation manifestation : oldSpiritWeb.getAvailableManifestations(true))
            {
                newSpiritWeb.giveManifestation(manifestation.getManifestationType(), manifestation.getPowerID());
            }

            CompoundNBT nbt = (CompoundNBT) storage.writeNBT(SpiritwebCapability.CAPABILITY, oldSpiritWeb, null);
            storage.readNBT(SpiritwebCapability.CAPABILITY, newSpiritWeb, null, nbt);
        }));
    }

    @SubscribeEvent
    public static void onTrackPlayer(PlayerEvent.StartTracking startTracking)
    {
        SpiritwebCapability.get(startTracking.getPlayer()).ifPresent(cap ->
        {
            cap.syncToClients(null);
        });
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        PlayerEntity player = event.player;

        //charge gemstones in inventory if the player is outside during a thunderstorm
        if (player.world.isRainingAt(player.getPosition()) && player.world.isThundering())
        {
            IInventory mainInv = player.inventory;
            int size = mainInv.getSizeInventory();

            for (int i = 0; i < size; i++)
            {
                IInventory inv = mainInv;
                int slot = i;

                ItemStack stackInSlot = inv.getStackInSlot(slot);
                //todo finish :D
                //if (stackInSlot != null && stackInSlot.getItem() instanceof ItemGemstone)
                {
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemTossEvent(ItemTossEvent event)
    {
        if (!event.getPlayer().world.isRemote)
        {
            //if (event.getEntityItem().getItem().getItem() instanceof ItemShardBlade)
            {
				/*
				//if we haven't got a shard blade in inv, put it in the inventory
				if (event.getPlayer().getCapability(CosmereCapabilities.SUMMON_SHARDBLADE, null).getInventoryShardblade().getStackInSlot(0) == null)
				{
					event.getPlayer().getCapability(CosmereCapabilities.SUMMON_SHARDBLADE, null).setIsShardbladeInInventory(true);

					ItemStack itemStack = event.getEntityItem().getEntityItem();
					ItemStack test = itemStack.copy();

					event.getPlayer().getCapability(CosmereCapabilities.SUMMON_SHARDBLADE, null).getInventoryShardblade().setInventorySlotContents(0, test);


					event.getEntityItem().isDead = true;
				}
				PacketDispatcher.sendTo(new SyncShardbladeData(event.getPlayer().getCapability(CosmereCapabilities.SUMMON_SHARDBLADE, null)), (EntityPlayerMP) event.getPlayer());
				*/
            }
        }
    }
}
