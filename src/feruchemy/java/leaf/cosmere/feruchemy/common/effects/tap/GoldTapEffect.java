/*
 * File updated ~ 26 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public class GoldTapEffect extends FeruchemyEffectBase
{
	public GoldTapEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				AttributesRegistry.HEALING_STRENGTH.getAttribute(),
				1.0D,
				AttributeModifier.Operation.ADDITION);
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, double strength)
	{
		if (!isActiveTick(entityLivingBaseIn))
		{
			return;
		}

		final int i = (int) (strength);
		if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth())
		{
			entityLivingBaseIn.heal(i);
		}

		//todo move to config
		final int timeToReduceByInTicks = i * 40;
		final int ticksNeededLeftToReduce = 5;

		//remove harmful effects over time
		for (MobEffectInstance activeEffect : entityLivingBaseIn.getActiveEffects())
		{
			if (!activeEffect.getEffect().isBeneficial() && activeEffect.getDuration() > ticksNeededLeftToReduce)
			{
				//never reduce down below 5 ticks
				final double clamped = Math.max(ticksNeededLeftToReduce, activeEffect.getDuration() - timeToReduceByInTicks);
				final int duration = Mth.floor(clamped);
				MobEffectInstance newInstance = new MobEffectInstance(
						activeEffect.getEffect(),
						duration,
						activeEffect.getAmplifier(),
						activeEffect.isAmbient(),
						activeEffect.isVisible(),
						activeEffect.showIcon());
				entityLivingBaseIn.removeEffectNoUpdate(activeEffect.getEffect());
				entityLivingBaseIn.addEffect(newInstance);
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
			int strength = (int) EntityHelper.getAttributeValue(event.getEntity(), AttributesRegistry.HEALING_STRENGTH.getAttribute(), 0);
			//take less damage when tapping
			if (strength > 6)
			{
				event.setAmount(event.getEntity().getHealth() - 1);
			}
		}
	}

}
