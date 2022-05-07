/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.MathHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyZinc extends AllomancyBase
{
	public AllomancyZinc(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//Inflames Emotions
	//make hostiles target you but also make non-hostiles target hostiles?
	@Override
	protected void performEffect(ISpiritweb data)
	{
		int mode = data.getMode(manifestationType, getMetalType().getID());

		//todo, replace x * mode with config based value
		double allomanticStrength = getStrength(data);

		int range = (int) (allomanticStrength * mode);

		List<LivingEntity> entitiesToAffect = getLivingEntitiesInRange(data.getLiving(), range, true);

		for (LivingEntity e : entitiesToAffect)
		{
			if (e instanceof Mob)
			{
				Mob mob = (Mob) e;

				//mob.targetSelector.enableFlag(Goal.Flag.TARGET);
				mob.setNoAi(false);

				switch (mode)
				{
					case 3:
						if (mob.getTarget() == null)
						{
							LivingEntity attackTarget = entitiesToAffect.get(MathHelper.randomInt(0, entitiesToAffect.size() - 1));
							mob.setTarget(attackTarget);
						}
					case 2:
						if (mob.getLastHurtByMob() == null)
						{
							mob.setLastHurtByMob(mob.getTarget() != null ? mob.getTarget()
							                                             : entitiesToAffect.get(MathHelper.randomInt(0, entitiesToAffect.size() - 1)));
						}

					case 1:
					default:
						mob.setAggressive(true);
				}
			}
		}

	}
}
