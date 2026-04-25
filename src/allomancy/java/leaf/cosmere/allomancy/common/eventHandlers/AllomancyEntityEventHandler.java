/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.effects.BrassStunEffect;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Allomancy.MODID, bus = EventBusSubscriber.Bus.GAME)
public class AllomancyEntityEventHandler
{

	@SubscribeEvent
	public static void onItemPickup(ItemEntityPickupEvent.Pre event)
	{
		if (CoinPouchItem.onPickupItem(event.getItemEntity(), event.getPlayer()))
		{
			event.setCanPickup(TriState.FALSE);
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

				MiscHelper.consumeNugget(target, stack, 1);
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
		final LivingEntity livingEntity = event.getEntity();

		if (event.getItem().getItem() instanceof MetalNuggetItem metalNuggetItem)
		{
			// Only consume the nugget if it contains Lerasium
			if (metalNuggetItem.getMetalType().isGodMetal() && metalNuggetItem.getMetalType() != Metals.MetalType.LERASIUM)
			{
				return;
			}
			MiscHelper.consumeNugget(livingEntity, event.getItem(), 1);
		}
	}


	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event)
	{
		AllomancyAtium.onLivingAttackEvent(event);
	}

	@SubscribeEvent
	public static void onLivingDamagePre(LivingDamageEvent.Pre event)
	{
		AllomancyNicrosil.onLivingHurtEvent(event);
		AllomancyPewter.onLivingHurtEvent(event);
		AllomancyChromium.onLivingHurtEvent(event);
	}

	@SubscribeEvent
	public static void onMobEffectRemoved(MobEffectEvent.Remove event)
	{
		MobEffectInstance instance = event.getEffectInstance();
		if (instance != null && instance.getEffect().value() instanceof BrassStunEffect)
		{
			BrassStunEffect.clearStun(event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onMobEffectExpired(MobEffectEvent.Expired event)
	{
		MobEffectInstance instance = event.getEffectInstance();
		if (instance != null && instance.getEffect().value() instanceof BrassStunEffect)
		{
			BrassStunEffect.clearStun(event.getEntity());
		}
	}
}
