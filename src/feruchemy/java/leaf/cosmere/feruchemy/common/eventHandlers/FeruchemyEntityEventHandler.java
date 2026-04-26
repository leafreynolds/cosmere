/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.eventHandlers;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import leaf.cosmere.common.items.MetalNuggetItem;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.effects.store.BrassStoreEffect;
import leaf.cosmere.feruchemy.common.effects.tap.GoldTapEffect;
import leaf.cosmere.feruchemy.common.utils.MiscHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Feruchemy.MODID, bus = EventBusSubscriber.Bus.GAME)
public class FeruchemyEntityEventHandler
{
	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity target) || event.getEntity().level().isClientSide)
		{
			return;
		}

		ItemStack stack = event.getEntity().getMainHandItem();
		if (!stack.isEmpty())
		{
			if (stack.getItem() instanceof MetalNuggetItem metalNuggetItem)
			{
				// Don't alloy for consuming normal metal nuggets
				if (!(stack.getItem() instanceof GodMetalAlloyNuggetItem) && !(stack.getItem() instanceof GodMetalNuggetItem)) return;
				// Only consume the nugget if it contains Lerasatium
				if (metalNuggetItem.getMetalType().isGodMetal() && metalNuggetItem.getMetalType() != Metals.MetalType.LERASATIUM) return;

				MiscHelper.consumeNugget(target, stack);
				stack.shrink(1);
			}
		}
	}


	@SubscribeEvent
	public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event)
	{
		final LivingEntity livingEntity = event.getEntity();
		if (event.getItem().getItem() instanceof MetalNuggetItem metalNuggetItem)
		{
			// Don't alloy for consuming normal metal nuggets
			if(!(event.getItem().getItem() instanceof GodMetalAlloyNuggetItem) && !(event.getItem().getItem() instanceof GodMetalNuggetItem)) return;
			// Only consume the nugget if it contains Lerasatium
			if(metalNuggetItem.getMetalType().isGodMetal() && metalNuggetItem.getMetalType() != Metals.MetalType.LERASATIUM) return;

			//no need to shrink item count as it's already done as part of nugget use item finish
			MiscHelper.consumeNugget(livingEntity, event.getItem());
		}
	}


	//Attack event happens first
	@SubscribeEvent
	public static void onLivingAttackEvent(LivingIncomingDamageEvent event)
	{
		BrassStoreEffect.onLivingAttackEvent(event);
	}

	//then living hurt event
	@SubscribeEvent
	public static void onLivingHurtEvent(LivingDamageEvent.Pre event)
	{
		BrassStoreEffect.onLivingHurtEvent(event);
		GoldTapEffect.onLivingHurtEvent(event);
	}


	// TODO: NeoForge 1.21.1 removed setExpToDrop from BlockEvent.BreakEvent.
	// The COSMERE_FORTUNE XP bonus can no longer be applied this way.
	// Item-drop fortune bonus is handled by FortuneBonusModifier (main module, Phase 9).
	// XP bonus will require a Mixin into Block#spawnAfterBreak or a GlobalLootModifier approach.
}
