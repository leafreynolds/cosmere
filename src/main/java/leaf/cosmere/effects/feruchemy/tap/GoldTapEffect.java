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
		if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth())
		{
			entityLivingBaseIn.heal(1);
		}

		//remove harmful effects over time
		for (MobEffectInstance activeEffect : entityLivingBaseIn.getActiveEffects())
		{
			if (!activeEffect.getEffect().isBeneficial() && activeEffect.getDuration() > 5)
			{
				MobEffectInstance effectInstance = new MobEffectInstance(
						activeEffect.getEffect(),
						Mth.floor(activeEffect.getDuration() * 0.8d),
						activeEffect.getAmplifier(),
						activeEffect.isAmbient(),
						activeEffect.isVisible(),
						activeEffect.showIcon());
				entityLivingBaseIn.removeEffectNoUpdate(activeEffect.getEffect());
				entityLivingBaseIn.addEffect(effectInstance);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		int k = 50 >> amplifier;
		if (k > 0) {
			return duration % k == 0;
		} else {
			return true;
		}
	}
}
