/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;

//Increases Physical Abilities
public class AllomancyPewter extends AllomancyManifestation
{
	public AllomancyPewter(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();

		//if we're here, we already know we are not compounding and have already 'paid' for the effect
		//if (!isCompounding(data))
		{
			final double strength = getStrength(data, false);
			//flaring gets extra out of the effect
			int actionableStrength = (int) Math.round(strength) * getMode(data);
			data.addEffect(EffectsHelper.getNewEffect(AllomancyEffects.ALLOMANTIC_PEWTER.getEffect(), livingEntity, actionableStrength));
		}
	}
}
