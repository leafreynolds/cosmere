/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class AllomancyPewter extends AllomancyBase
{
	public AllomancyPewter(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void performEffect(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;

		//Increases Physical Abilities
		if (isActiveTick)
		{
			int mode = data.getMode(manifestationType, getMetalType().getID());
			livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SPEED, 0));
			switch (mode)
			{
				case 3:
				case 2:
					livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DIG_SPEED, 0));
					livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DAMAGE_RESISTANCE, mode - 2));
				case 1:
					livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DAMAGE_BOOST, mode - 1));
					break;
			}
		}
	}
}
