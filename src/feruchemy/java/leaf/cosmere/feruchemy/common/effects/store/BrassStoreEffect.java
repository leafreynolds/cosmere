/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import leaf.cosmere.feruchemy.common.registries.FeruchemyEffects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
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

		MobEffectInstance effectInstance = event.getEntity().getEffect(FeruchemyEffects.STORING_EFFECTS.get(Metals.MetalType.BRASS).get());
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

		MobEffectInstance effectInstance = event.getEntity().getEffect(FeruchemyEffects.STORING_EFFECTS.get(Metals.MetalType.BRASS).get());
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
					if (event.getEntity().isOnFire())
					{
						event.getEntity().clearFire();
					}
					event.setCanceled(true);
			}
		}
	}

}
