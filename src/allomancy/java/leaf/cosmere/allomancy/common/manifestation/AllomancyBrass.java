/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.List;

public class AllomancyBrass extends AllomancyManifestation
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

		List<LivingEntity> entitiesToAffect = EntityHelper.getLivingEntitiesInRange(data.getLiving(), range, true);

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
