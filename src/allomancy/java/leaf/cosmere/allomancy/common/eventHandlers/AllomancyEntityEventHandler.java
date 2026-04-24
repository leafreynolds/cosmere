/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.utils.MiscHelper;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import leaf.cosmere.common.items.MetalNuggetItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Allomancy.MODID, bus = EventBusSubscriber.Bus.GAME)
public class AllomancyEntityEventHandler
{

	// TODO Phase 11.3: re-target to ItemEntityPickupEvent.Pre (EntityItemPickupEvent removed in NeoForge 1.21.1).
	//                  Old body called CoinPouchItem.onPickupItem(event.getItem(), event.getEntity()) and event.setCanceled(true).

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


	// TODO Phase 11.3: re-target to LivingIncomingDamageEvent (LivingAttackEvent removed in NeoForge 1.21.1).
	//                  Old body called AllomancyAtium.onLivingAttackEvent(event).

	// TODO Phase 11.3: re-target to LivingIncomingDamageEvent or LivingDamageEvent.Pre (LivingHurtEvent removed in NeoForge 1.21.1).
	//                  Old body called AllomancyNicrosil/Pewter/Chromium.onLivingHurtEvent(event) in that order.
}
