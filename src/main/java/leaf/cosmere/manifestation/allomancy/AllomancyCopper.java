/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyCopper extends AllomancyBase
{
	public AllomancyCopper(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void performEffect(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;

		//Hides Allomantic Pulses
		if (isActiveTick)
		{
			int mode = getMode(data);
			MobEffectInstance newEffect = EffectsHelper.getNewEffect(EffectsRegistry.ALLOMANTIC_COPPER.get(), mode - 1);

			List<LivingEntity> entitiesToApplyEffect = getLivingEntitiesInRange(livingEntity, 5, true);

			for (LivingEntity e : entitiesToApplyEffect)
			{
				e.addEffect(newEffect);
			}
		}
	}
}
