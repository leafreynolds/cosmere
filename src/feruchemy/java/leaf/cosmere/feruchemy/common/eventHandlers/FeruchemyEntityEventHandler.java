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
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyAtium;
import leaf.cosmere.feruchemy.common.utils.MiscHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Feruchemy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
		if (event.isCanceled())
		{
			return;
		}

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
		GoldTapEffect.onLivingHurtEvent(event);
	}


	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onBreakBlock(BlockEvent.BreakEvent evt)
	{
		Player player = evt.getPlayer();

		int totalFortuneBonus = (int) EntityHelper.getAttributeValue(player, AttributesRegistry.COSMERE_FORTUNE.getAttribute());

		if (totalFortuneBonus == 0)
		{
			return;
		}

		ItemStack stack = player.getMainHandItem();
		int bonusLevel = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
		int silklevel = stack.getEnchantmentLevel(Enchantments.SILK_TOUCH);

		LevelAccessor level = evt.getLevel();
		evt.setExpToDrop(
				evt.getState().getExpDrop(
						level,
						level.getRandom(),
						evt.getPos(),
						bonusLevel + totalFortuneBonus,
						silklevel
				)
		);
	}
}
