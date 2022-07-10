/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

//warmth
public class BrassStoreEffect extends FeruchemyEffectBase
{
	public BrassStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
	}

	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (!event.getSource().isFire() || event.isCanceled())
		{
			return;
		}

		MobEffectInstance effectInstance = event.getEntityLiving().getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.BRASS).get());
		if (effectInstance != null && effectInstance.getDuration() > 0)
		{
			final float amount;
			switch (effectInstance.getAmplifier())
			{
				case 0:
					amount = event.getAmount() / 2;
					break;
				case 1:
					amount = event.getAmount() / 4;
					break;
				default:
				case 2:
					event.setCanceled(true);
					return;
			}
			event.setAmount(amount);
		}
	}


	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		if (!event.getSource().isFire() || event.isCanceled())
		{
			return;
		}

		MobEffectInstance effectInstance = event.getEntityLiving().getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.BRASS).get());
		if (effectInstance != null && effectInstance.getDuration() > 0)
		{
			switch (effectInstance.getAmplifier())
			{
				case 0:
				case 1:
				case 2:
					break;
				default:
				case 3:
					if (event.getEntityLiving().isOnFire())
					{
						event.getEntityLiving().clearFire();
					}
					event.setCanceled(true);
			}
		}
	}

}
