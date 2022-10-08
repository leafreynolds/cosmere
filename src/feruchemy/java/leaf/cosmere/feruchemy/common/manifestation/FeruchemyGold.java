/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class FeruchemyGold extends FeruchemyManifestation
{
	public FeruchemyGold(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int getCost(ISpiritweb data)
	{
		final int cost = super.getCost(data);
		//todo config gold tap cost
		return isTapping(data) ? cost * 10 : cost;
	}

	@Override
	public void tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		//don't heal more than needed.
		if (livingEntity.tickCount % 20 != 0)
		{
			return;
		}

		if (isTapping(data) && livingEntity.getHealth() >= livingEntity.getMaxHealth())
		{
			return;
		}

		// we charge them for tapping,
		// or if storing, we only want to add to their metalmind every so often. Otherwise gold is too strong.
		final boolean spend = isTapping(data) || livingEntity.tickCount % 100 == 0;

		//if we are simulating, spend == false
		if (canAfford(data, !spend))//success
		{
			applyEffectTick(data);
		}
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		super.applyEffectTick(data);
		final int mode = getMode(data);
		MobEffect effect = getEffect(mode);
		MobEffectInstance currentEffect = EffectsHelper.getNewEffect(effect, Math.abs(mode) - 1);

		if (effect == null)
		{
			return;
		}

		data.getLiving().addEffect(currentEffect);
	}
}
