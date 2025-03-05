/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.common.effects;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.spiritweb.ISpiritweb;

import java.util.Map;

public class DrainInvestitureEffect extends CosmereEffect
{
	@Override
	protected int getTickOffset()
	{
		return Metals.MetalType.NICROSIL.getID();
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		//todo boost metal drain balancing

		final Map<Manifestations.ManifestationTypes, ISpiritwebSubmodule> submodules = data.getSubmodules();
		for (ISpiritwebSubmodule module : submodules.values())
		{
			module.drainInvestiture(data, strength);
		}
	}
}
