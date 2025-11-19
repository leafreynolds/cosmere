/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import leaf.cosmere.allomancy.common.manifestation.AllomancyAtium;
import leaf.cosmere.allomancy.common.manifestation.AllomancyChromium;
import leaf.cosmere.allomancy.common.manifestation.AllomancyNicrosil;
import leaf.cosmere.allomancy.common.manifestation.AllomancyPewter;
import leaf.cosmere.allomancy.common.utils.MiscHelper;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
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
			if (stack.getItem() instanceof MetalNuggetItem metalNuggetItem)
			{
				// Don't alloy for consuming normal metal nuggets
				if (!(stack.getItem() instanceof GodMetalAlloyNuggetItem) && !(stack.getItem() instanceof GodMetalNuggetItem))
				{
					return;
				}
				// Only consume the nugget if it contains Lerasium
				if (metalNuggetItem.getMetalType().isGodMetal() && metalNuggetItem.getMetalType() != Metals.MetalType.LERASIUM)
				{
					return;
				}

				MiscHelper.consumeNugget(target, stack);
				stack.shrink(1);
			}
		}
		else
		{

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

		if (event.getItem().getItem() instanceof MetalNuggetItem metalNuggetItem)
		{
			// Only consume the nugget if it contains Lerasium
			if (metalNuggetItem.getMetalType().isGodMetal() && metalNuggetItem.getMetalType() != Metals.MetalType.LERASIUM)
			{
				return;
			}
			MiscHelper.consumeNugget(livingEntity, event.getItem());
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
		AllomancyPewter.onLivingHurtEvent(event);
		AllomancyChromium.onLivingHurtEvent(event);
	}
}
