/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class AllomancyDuralumin extends AllomancyBase
{
	public AllomancyDuralumin(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//Enhances Current Metals Burned
	@Override
	public void performEffect(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;

		if (isActiveTick)
		{
			//apply the effect regardless, because duralumin is currently active.
			MobEffectInstance newEffect = EffectsHelper.getNewEffect(
					EffectsRegistry.ALLOMANCY_BOOST.get(),
					Mth.floor(getStrength(data))
			);
			data.getLiving().addEffect(newEffect);
		}
	}
}
