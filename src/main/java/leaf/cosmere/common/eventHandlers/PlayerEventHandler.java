/*
 * File updated ~ 26 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

@EventBusSubscriber(modid = Cosmere.MODID, bus = EventBusSubscriber.Bus.GAME)
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

					newSpiritWeb.onPlayerClone(event, oldSpiritWeb);
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
	public static void onItemTossEvent(ItemTossEvent event)
	{
		if (!event.getPlayer().level().isClientSide)
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
	public static void onXPChange(PlayerXpEvent.XpChange event)
	{
		boolean isRemote = event.getEntity().level().isClientSide;
		if (isRemote)
		{
			return;
		}

		AttributeRegistryObject<Attribute> xpGainRateAttribute = AttributesRegistry.XP_RATE_ATTRIBUTE;
		Holder<Attribute> holder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(xpGainRateAttribute.get());
		AttributeInstance attribute = event.getEntity().getAttribute(holder);
		if (attribute != null)
		{
			event.setAmount((int) (event.getAmount() * attribute.getValue()));
		}
	}
}
