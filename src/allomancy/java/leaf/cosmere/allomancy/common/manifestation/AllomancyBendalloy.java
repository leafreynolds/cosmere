/*
 * File updated ~ 7 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AllomancyBendalloy extends AllomancyManifestation
{
	public AllomancyBendalloy(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		boolean isActiveTick = isActiveTick(data);

		//Slows Down Time for the entities around the user
		if (isActiveTick)
		{
			int mode = getMode(data);

			int range = this.getRange(data);

			List<LivingEntity> entitiesToAffect = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, false);

			for (LivingEntity e : entitiesToAffect)
			{
				e.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SLOWDOWN, mode));
			}

			//todo slow tile entities? not sure how to do that. cadmium just calls tick more often.
		}
	}
}
