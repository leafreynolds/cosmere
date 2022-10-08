/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler
{
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event)
	{
		event.getOriginal().revive();

		SpiritwebCapability.get(event.getOriginal()).ifPresent((oldSpiritWeb) ->
				SpiritwebCapability.get(event.getEntity()).ifPresent((newSpiritWeb) ->
				{
					//copy across anything from the old player if needed
					//Metals ingested?
					//Stormlight?

					newSpiritWeb.transferFrom(oldSpiritWeb);
				}));
	}

	@SubscribeEvent
	public static void onTrackPlayer(PlayerEvent.StartTracking startTracking)
	{
		SpiritwebCapability.get(startTracking.getEntity()).ifPresent(cap ->
		{
			cap.syncToClients(null);
		});
	}

	@SubscribeEvent
	public void onItemTossEvent(ItemTossEvent event)
	{
		if (!event.getPlayer().level.isClientSide)
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

	@SubscribeEvent
	public void onXPChange(PlayerXpEvent.XpChange event)
	{
		boolean isRemote = event.getEntity().level.isClientSide;
		if (isRemote)
		{
			return;
		}

		AttributeRegistryObject<Attribute> xpGainRateAttribute = AttributesRegistry.XP_RATE_ATTRIBUTE;
		AttributeInstance attribute = event.getEntity().getAttribute(xpGainRateAttribute.get());
		if (attribute != null)
		{
			event.setAmount((int) (event.getAmount() * attribute.getValue()));
		}
	}
}
