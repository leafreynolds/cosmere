/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

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
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;

		//Increases Physical Abilities
		if (isActiveTick)
		{
			int mode = getMode(data);
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
