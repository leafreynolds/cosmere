/*
 * File updated ~ 8 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;

public class AllomancyDuralumin extends AllomancyManifestation
{
	public AllomancyDuralumin(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//Enhances Current Metals Burned
	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;

		if (isActiveTick)
		{
			//apply the effect regardless, because duralumin is currently active.
			CosmereEffectInstance newEffect = EffectsHelper.getNewEffect(
					AllomancyEffects.ALLOMANCY_BOOST.get(),
					data.getLiving(),
					getStrength(data, false)
			);
			data.addEffect(newEffect);
		}
	}
}
