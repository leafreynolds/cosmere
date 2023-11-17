/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.eventHandlers;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.MetalNuggetItem;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.effects.store.BrassStoreEffect;
import leaf.cosmere.feruchemy.common.effects.tap.GoldTapEffect;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyAtium;
import leaf.cosmere.feruchemy.common.utils.MiscHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
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

				switch (metalType)
				{
					//only care about god metal when trying to give others powers
					case LERASATIUM:
						MiscHelper.consumeNugget(target, metalType);
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
		if (event.getItem().getItem() instanceof MetalNuggetItem item && item.getMetalType() == Metals.MetalType.LERASATIUM)
		{
			//no need to shrink item count as it's already done as part of nugget use item finish
			MiscHelper.consumeNugget(livingEntity, Metals.MetalType.LERASATIUM);
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


}
