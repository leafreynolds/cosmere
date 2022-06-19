/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyBrass extends AllomancyBase
{
	public AllomancyBrass(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//Dampens Emotions
	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);

		//todo, replace x * mode with config based value
		double allomanticStrength = getStrength(data, false);

		int range = getRange(data);

		List<LivingEntity> entitiesToAffect = getLivingEntitiesInRange(data.getLiving(), range, true);

		for (LivingEntity e : entitiesToAffect)
		{
			if (e instanceof Mob mob)
			{
				mob.setNoAi(mode == 3 && allomanticStrength > 15);

				switch (mode)
				{
					case 2:
						mob.setTarget(null);
					case 1:
						mob.setAggressive(false);
					default:
						//stop angry targets from attacking things
						e.setLastHurtByMob(null);

				}
			}
		}
	}
}
