/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import leaf.cosmere.feruchemy.common.registries.FeruchemyEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public class GoldTapEffect extends FeruchemyEffectBase
{
	public GoldTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		if (!isActiveTick(entityLivingBaseIn))
		{
			return;
		}

		final int i = 1 + amplifier;
		if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth())
		{
			entityLivingBaseIn.heal(i);
		}

		//remove harmful effects over time
		for (MobEffectInstance activeEffect : entityLivingBaseIn.getActiveEffects())
		{
			if (!activeEffect.getEffect().isBeneficial() && activeEffect.getDuration() > 5)
			{
				double reduceAmount = 1 - (i / 10d);

				MobEffectInstance effectInstance = new MobEffectInstance(
						activeEffect.getEffect(),
						Mth.floor(activeEffect.getDuration() * reduceAmount),
						activeEffect.getAmplifier(),
						activeEffect.isAmbient(),
						activeEffect.isVisible(),
						activeEffect.showIcon());
				entityLivingBaseIn.removeEffectNoUpdate(activeEffect.getEffect());
				entityLivingBaseIn.addEffect(effectInstance);
			}
		}
	}

	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (event.isCanceled())
		{
			return;
		}

		if (event.getAmount() > event.getEntity().getHealth())
		{
			MobEffectInstance tapEffect = event.getEntity().getEffect(FeruchemyEffects.TAPPING_EFFECTS.get(Metals.MetalType.GOLD).get());

			//take less damage when tapping
			if (tapEffect != null && tapEffect.getDuration() > 0 && tapEffect.getAmplifier() > 6)
			{
				event.setAmount(event.getEntity().getHealth() - 1);
			}
		}
	}

}
