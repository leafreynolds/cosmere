/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
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
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = Feruchemy.MODID)
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
				if (!(stack.getItem() instanceof GodMetalAlloyNuggetItem) && !(stack.getItem() instanceof GodMetalNuggetItem))
				{
					return;
				}
				// Only consume the nugget if it contains Lerasatium
				if (metalNuggetItem.getMetalType().isGodMetal() && metalNuggetItem.getMetalType() != Metals.MetalType.LERASATIUM)
				{
					return;
				}

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
			if (!(event.getItem().getItem() instanceof GodMetalAlloyNuggetItem) && !(event.getItem().getItem() instanceof GodMetalNuggetItem))
			{
				return;
			}
			// Only consume the nugget if it contains Lerasatium
			if (metalNuggetItem.getMetalType().isGodMetal() && metalNuggetItem.getMetalType() != Metals.MetalType.LERASATIUM)
			{
				return;
			}

			//no need to shrink item count as it's already done as part of nugget use item finish
			MiscHelper.consumeNugget(livingEntity, event.getItem());
		}
	}


	//LivingAttackEvent and LivingHurtEvent were merged into LivingIncomingDamageEvent
	@SubscribeEvent
	public static void onLivingHurtEvent(LivingIncomingDamageEvent event)
	{
		BrassStoreEffect.onLivingHurtEvent(event);
		GoldTapEffect.onLivingHurtEvent(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onBlockDrops(BlockDropsEvent evt)
	{
		if (!(evt.getBreaker() instanceof Player player))
		{
			return;
		}

		int totalFortuneBonus = (int) EntityHelper.getAttributeValue(player, AttributesRegistry.COSMERE_FORTUNE.getHolder());

		if (totalFortuneBonus == 0)
		{
			return;
		}

		LevelAccessor level = evt.getLevel();

		//exp drops are based on the tool stack now,
		//so hand the block a copy with the extra fortune baked in
		ItemStack tool = evt.getTool();
		ItemStack fakeTool = tool.isEmpty() ? new ItemStack(Items.BARRIER) : tool.copy();

		Holder<Enchantment> fortune = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
		fakeTool.enchant(fortune, fakeTool.getEnchantmentLevel(fortune) + totalFortuneBonus);

		evt.setDroppedExperience(
				evt.getState().getExpDrop(
						level,
						evt.getPos(),
						evt.getBlockEntity(),
						player,
						fakeTool
				)
		);
	}
}
