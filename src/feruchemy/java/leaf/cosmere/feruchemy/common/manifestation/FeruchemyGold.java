/*
 * File updated ~ 7 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.feruchemy.common.config.FeruchemyConfigs;

// Todo for TLC:
//      - rebalance death resistance
//      - 1 tap should be half a heart every 10-20 seconds
//      - max tap (32?) will be half a heart every tick // VERY EXPENSIVE, should drain faster than max compounding
//      - keep tapping at max health (sorry all y'all bloodmakers) buff max health instead at a high enough tap rate (3+?). Half a heart per tap level above?

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

	public static int getHealActiveTick(int strength)
	{
		return (int) Math.floor(200*Math.pow(strength, -1.5D));
	}
}
