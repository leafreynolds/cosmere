/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import leaf.cosmere.allomancy.common.manifestation.AllomancyAtium;
import leaf.cosmere.allomancy.common.manifestation.AllomancyNicrosil;
import leaf.cosmere.allomancy.common.utils.MiscHelper;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.MetalNuggetItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Allomancy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyEntityEventHandler
{


	@SubscribeEvent
	public static void onEntityItemPickUp(EntityItemPickupEvent event)
	{
		if (CoinPouchItem.onPickupItem(event.getItem(), event.getEntity()))
		{
			event.setCanceled(true);
		}
	}


	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity target))
		{
			return;
		}

		ItemStack stack = event.getEntity().getMainHandItem();
		if (!stack.isEmpty())
		{
			if (stack.getItem() instanceof MetalNuggetItem beadItem)
			{
				Metals.MetalType metalType = beadItem.getMetalType();

				switch (metalType)
				{
					//only care about god metal when trying to give others powers
					case LERASATIUM:
					case LERASIUM:
						MiscHelper.consumeNugget(target, metalType, 1);
						//need to shrink, because metal nugget only shrinks on item use finish from eating
						stack.shrink(1);
						break;
				}

			}
		}
	}


	@SubscribeEvent
	public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event)
	{
		if (event.isCanceled())
		{
			return;
		}

		final LivingEntity livingEntity = event.getEntity();
		ItemStack stack = livingEntity.getMainHandItem();
		if (!stack.isEmpty())
		{
			if (stack.getItem() instanceof MetalNuggetItem beadItem)
			{
				MiscHelper.consumeNugget(livingEntity, beadItem.getMetalType(), 1);
				//no need to shrink item count, as the item itself does that on finish using item
			}
		}
	}


	//Attack event happens first
	@SubscribeEvent
	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		AllomancyAtium.onLivingAttackEvent(event);
	}

	//then living hurt event
	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		AllomancyNicrosil.onLivingHurtEvent(event);
	}


}
