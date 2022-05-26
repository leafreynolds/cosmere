/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;


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

		if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth())
		{
			entityLivingBaseIn.heal(1);
		}

		//remove harmful effects over time
		for (MobEffectInstance activeEffect : entityLivingBaseIn.getActiveEffects())
		{
			if (!activeEffect.getEffect().isBeneficial() && activeEffect.getDuration() > 5)
			{
				double reduceAmount = 1 - ((1 + amplifier) / 10d);

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

}
