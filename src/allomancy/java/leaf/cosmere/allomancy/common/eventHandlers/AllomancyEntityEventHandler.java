/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 * File updated ~ 2026-05-02 ~ Leaf (merged Mistcloak v2 from develop)
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.effects.BrassStunEffect;
import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import leaf.cosmere.allomancy.common.items.MistcloakItem;
import leaf.cosmere.allomancy.common.manifestation.AllomancyAtium;
import leaf.cosmere.allomancy.common.manifestation.AllomancyChromium;
import leaf.cosmere.allomancy.common.manifestation.AllomancyNicrosil;
import leaf.cosmere.allomancy.common.manifestation.AllomancyPewter;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.utils.MiscHelper;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CuriosHelper;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import leaf.cosmere.common.items.MetalNuggetItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

@EventBusSubscriber(modid = Allomancy.MODID, bus = EventBusSubscriber.Bus.GAME)
public class AllomancyEntityEventHandler
{

	private static final List<ResourceKey<DamageType>> protectedDamageTypes =
			List.of(DamageTypes.FALL,
					DamageTypes.FLY_INTO_WALL
					//DamageTypes.FREEZE
			);

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

		// Mistcloak fall/wall damage absorption (ported from develop's Mistcloak v2 PR).
		// LivingHurtEvent (1.20.1) -> LivingDamageEvent.Pre (1.21.1); use getNewDamage/setNewDamage.
		final DamageSource source = event.getSource();
		final LivingEntity entity = event.getEntity();

		// Skip if client side or no damage to absorb
		if (entity.level().isClientSide || event.getNewDamage() <= 0)
		{
			return;
		}

		if (CuriosHelper.getCuriosHandler(entity).isPresent()
				&& CuriosHelper.hasItemInInventory(entity, AllomancyItems.MISTCLOAK.asItem()))
		{
			// Only absorb damage types that the mistcloak protects against.
			for (ResourceKey<DamageType> type : protectedDamageTypes)
			{
				if (!source.is(type))
				{
					continue;
				}
				for (SlotResult slotResult : CuriosHelper.getSlotsWithItem(entity, AllomancyItems.MISTCLOAK.asItem()))
				{
					ItemStack stack = slotResult.stack();
					MistcloakItem item = (MistcloakItem) stack.getItem();
					// 1.21.1: Item.getDamage/getMaxDamage(ItemStack) removed; use stack accessors directly.
					if (stack.getDamageValue() >= stack.getMaxDamage())
					{
						continue;
					}

					float original = event.getNewDamage();
					float absorbed = original * 0.2f;
					float remaining = original - absorbed;

					// Reduce damage taken by entity
					event.setNewDamage(remaining);

					int damage = (int) (stack.getDamageValue() + Math.ceil(absorbed));
					stack.setDamageValue(damage);

					// If we successfully reduce damage, we don't need to keep going.
					if (stack.getDamageValue() >= stack.getMaxDamage())
					{
						item.curioBreak(slotResult.slotContext(), stack);
					}
					break;
				}
			}
		}
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
