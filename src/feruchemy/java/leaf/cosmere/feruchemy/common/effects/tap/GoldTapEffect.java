/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyGold;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public class GoldTapEffect extends FeruchemyEffectBase
{
	private static final int MIN_TAP_FOR_ABSORPTION = 5;
	private float absorbtionLastHealTick = 0F;
	private boolean isHealInit = false;

	public GoldTapEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				AttributesRegistry.HEALING_STRENGTH.getAttribute(),
				1.0D,
				AttributeModifier.Operation.ADDITION);
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		final LivingEntity living = data.getLiving();
		final int i = (int) (strength);
		boolean isHealTick;

		// at min tap (1) should be active every 10 seconds     // 10 seconds is 200 ticks
		int healActiveTick = FeruchemyGold.getHealActiveTick(i);
		healActiveTick = Math.max(1, healActiveTick);   // can't tick more often than every tick
		isHealTick = living.tickCount % healActiveTick == 0;

		if (isHealTick)
		{
			final int amountToHeal = 1;
			if (living.getHealth() >= living.getMaxHealth() && i >= MIN_TAP_FOR_ABSORPTION)  // if health is at max and tapping 7+
			{
				float maxAbsorption = i - MIN_TAP_FOR_ABSORPTION + 1F;
				float absorptionAmount = living.getAbsorptionAmount();

				if (absorptionAmount < maxAbsorption)
				{
					living.setAbsorptionAmount(absorptionAmount + 0.5F);
				}
			}
			else
			{
				living.heal(amountToHeal);
			}
		}

		if (living.tickCount % (getActiveTick() + getTickOffset()) == 0)
		{
			//todo move to config
			final int timeToReduceByInTicks = i * 40;
			final int ticksNeededLeftToReduce = 5;

			//remove harmful effects over time
			for (MobEffectInstance activeEffect : living.getActiveEffects())
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
					living.removeEffectNoUpdate(activeEffect.getEffect());
					living.addEffect(newInstance);
				}
			}
		}
	}

	@Override
	protected boolean isActiveTick(ISpiritweb data)
	{
		return true;
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
