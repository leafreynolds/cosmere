/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.List;

public class AllomancyZinc extends AllomancyManifestation
{
	public AllomancyZinc(Metals.MetalType metalType)
	{
		super(metalType);
	}

	//Inflames Emotions
	//make hostiles target you but also make non-hostiles target hostiles?
	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);
		int range = getRange(data);

		List<LivingEntity> entitiesToAffect = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, true);

		for (LivingEntity e : entitiesToAffect)
		{
			if (e instanceof Mob mob)
			{

				//mob.targetSelector.enableFlag(Goal.Flag.TARGET);
				mob.setNoAi(false);

				switch (mode)
				{
					case 3:
						if (mob.getTarget() == null)
						{
							LivingEntity attackTarget = entitiesToAffect.get(MathHelper.RANDOM.nextInt(entitiesToAffect.size()));
							mob.setTarget(attackTarget);
						}
					case 2:
						if (mob.getLastHurtByMob() == null)
						{
							mob.setLastHurtByMob(mob.getTarget() != null
							                     ? mob.getTarget()
							                     : entitiesToAffect.get(MathHelper.RANDOM.nextInt(entitiesToAffect.size())));
						}

					case 1:
					default:
						mob.setAggressive(true);
				}
			}
		}

	}
}
