/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
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
		MinecraftForge.EVENT_BUS.addListener(this::onLivingHurtEvent);
		MinecraftForge.EVENT_BUS.addListener(this::onLivingAttackEvent);
	}

	public void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (!event.getSource().isFire())
		{
			return;
		}

		MobEffectInstance effectInstance = event.getEntityLiving().getEffect(this);
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


	public void onLivingAttackEvent(LivingAttackEvent event)
	{
		if (!event.getSource().isFire())
		{
			return;
		}

		MobEffectInstance effectInstance = event.getEntityLiving().getEffect(this);
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
