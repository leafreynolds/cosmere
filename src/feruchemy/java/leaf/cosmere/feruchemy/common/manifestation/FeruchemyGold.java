/*
 * File updated ~ 14 - 02 - 2024 ~ Gerbagel
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.feruchemy.common.config.FeruchemyConfigs;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

// Todo for TLC:
//      - rebalance death resistance
//      - max tap (32?) will be half a heart every tick // VERY EXPENSIVE, should drain faster than max compounding

public class FeruchemyGold extends FeruchemyManifestation
{
	private static final int MIN_TAP_FOR_EXTRA_HEALTH = 5;
	public FeruchemyGold(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int getCost(ISpiritweb data)
	{
		final int cost = super.getCost(data);
		return isTapping(data)
		       ? cost * FeruchemyConfigs.SERVER.GOLD_TAP_COST_MULTIPLIER.get()
		       : cost;
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		//don't heal more than needed.
		if (!isActiveTick(data))
		{
			return false;
		}

		// we charge them for tapping,
		// or if storing, we only want to add to their metalmind every so often. Otherwise gold is too strong.
		final boolean spend = isTapping(data) || getActiveTick(data) % 100 == 0;

		//if we are simulating, spend == false
		if (canAfford(data, !spend))//success
		{
			applyEffectTick(data);
			//todo, decide if gold should always trigger sculk
			//spiritual healing seems like it would be kinda loud, right?
			return isTapping(data);
		}
		return false;
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);
		CosmereEffect effect = getEffect(mode);
		CosmereEffectInstance currentEffect = EffectsHelper.getNewEffect(effect, data.getLiving(), Math.abs(mode));//todo check this strength

		if (mode <= 0)
		{
			int bonusHealth = Math.max(0, -mode - MIN_TAP_FOR_EXTRA_HEALTH + 1);

			currentEffect.setDynamicAttribute(Attributes.MAX_HEALTH, (double) bonusHealth / -mode, AttributeModifier.Operation.ADDITION);
		}

		data.addEffect(currentEffect);

		// this updates health immediately rather than waiting for a health update
		data.getLiving().setHealth(data.getLiving().getHealth());
	}

	public static int getHealActiveTick(int strength)
	{
		return (int) Math.floor(200*Math.pow(strength, -1.5D));
	}
}
