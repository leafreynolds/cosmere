/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;

import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler
{
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event)
	{
		Capability.IStorage<ISpiritweb> storage = SpiritwebCapability.CAPABILITY.getStorage();
		event.getOriginal().revive();
		SpiritwebCapability.get(event.getOriginal()).ifPresent((oldSpiritWeb) ->
				SpiritwebCapability.get(event.getPlayer()).ifPresent((newSpiritWeb) ->
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

                    for (Metals.MetalType metalType : Metals.MetalType.values())
                    {
	                    //check for others
	                    final RegistryObject<Attribute> metalRelatedAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getName());
	                    if (metalRelatedAttribute != null && metalRelatedAttribute.isPresent())
	                    {
		                    ModifiableAttributeInstance oldPlayerAttribute = event.getOriginal().getAttribute(metalRelatedAttribute.get());
		                    ModifiableAttributeInstance newPlayerAttribute = event.getPlayer().getAttribute(metalRelatedAttribute.get());

		                    if (newPlayerAttribute != null && oldPlayerAttribute != null)
		                    {
			                    newPlayerAttribute.setBaseValue(oldPlayerAttribute.getBaseValue());
		                    }

	                    }
                    }
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
		if (player.level.isRainingAt(player.blockPosition()) && player.level.isThundering())
		{
			IInventory mainInv = player.inventory;
			int size = mainInv.getContainerSize();

			for (int i = 0; i < size; i++)
			{
				IInventory inv = mainInv;
				int slot = i;

				ItemStack stackInSlot = inv.getItem(slot);
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
		boolean isRemote = event.getEntityLiving().level.isClientSide;
		if (isRemote)
		{
			return;
		}

		//Feruchemical Zinc
		{
			EffectInstance tappingZincEffect = event.getPlayer().getEffect(EffectsRegistry.TAPPING_EFFECTS.get(Metals.MetalType.ZINC).get());
			EffectInstance storingZincEffect = event.getPlayer().getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.ZINC).get());

			if (tappingZincEffect != null)
			{
				event.setAmount(event.getAmount() * (tappingZincEffect.getAmplifier() + 2));
			}
			if (storingZincEffect != null)
			{
				event.setAmount(event.getAmount() / (storingZincEffect.getAmplifier() + 2));
			}
		}

		RegistryObject<Attribute> xpGainRateAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(Metals.MetalType.COPPER.getName());
		if (xpGainRateAttribute != null && xpGainRateAttribute.isPresent())
		{
			ModifiableAttributeInstance attribute = event.getPlayer().getAttribute(xpGainRateAttribute.get());
			if (attribute != null)
			{
				event.setAmount((int) (event.getAmount() * attribute.getValue()));
			}
		}
	}
}
