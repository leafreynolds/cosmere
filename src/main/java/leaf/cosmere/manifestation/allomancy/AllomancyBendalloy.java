/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyBendalloy extends AllomancyBase
{
	public AllomancyBendalloy(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void performEffect(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;

		//Slows Down Time for the entities around the user
		if (isActiveTick)
		{
			int mode = data.getMode(manifestationType, getMetalType().getID());

			int range = 5 * mode;

			List<LivingEntity> entitiesToAffect = getLivingEntitiesInRange(data.getLiving(), range, true);

			for (LivingEntity e : entitiesToAffect)
			{
				e.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SLOWDOWN, mode));
			}

			//todo slow tile entities? not sure how to do that. cadmium just calls tick more often.
		}
	}
}
