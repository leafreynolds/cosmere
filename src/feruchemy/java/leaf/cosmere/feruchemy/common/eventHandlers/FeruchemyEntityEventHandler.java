/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.MetalNuggetItem;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.effects.store.BrassStoreEffect;
import leaf.cosmere.feruchemy.common.effects.tap.GoldTapEffect;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyAtium;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyElectrum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Feruchemy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FeruchemyEntityEventHandler
{
	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity target) || event.getEntity().level.isClientSide)
		{
			return;
		}

		ItemStack stack = event.getEntity().getMainHandItem();
		if (!stack.isEmpty())
		{
			if (stack.getItem() instanceof MetalNuggetItem beadItem)
			{
				Metals.MetalType metalType = beadItem.getMetalType();

				if (metalType == Metals.MetalType.LERASATIUM)
				{
					SpiritwebCapability.get(target).ifPresent(iSpiritweb ->
					{
						SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

						for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
						{
							//give feruchemy
							if (manifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY)
							{
								//todo config feruchemy strength
								spiritweb.giveManifestation(manifestation, 13);
							}
						}

						spiritweb.syncToClients(null);

					});
				}
			}
		}
	}

	@SubscribeEvent
	public static void changeSize(EntityEvent.Size event)
	{
		final Entity entity = event.getEntity();
		if (entity instanceof LivingEntity livingEntity)
		{
			float scale = FeruchemyAtium.getScale(livingEntity);

			//only change if scale not 1, else we let the change size event do it's thing unimpeded
			if (scale != 1)
			{
				event.setNewSize(event.getNewSize().scale(scale));
				event.setNewEyeHeight(event.getNewEyeHeight() * scale);
			}
		}
	}

	//Attack event happens first
	@SubscribeEvent
	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		BrassStoreEffect.onLivingAttackEvent(event);
	}

	//then living hurt event
	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		BrassStoreEffect.onLivingHurtEvent(event);
		FeruchemyElectrum.onLivingHurtEvent(event);
		GoldTapEffect.onLivingHurtEvent(event);
	}


}
