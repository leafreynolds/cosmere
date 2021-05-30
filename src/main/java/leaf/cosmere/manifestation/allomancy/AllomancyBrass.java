/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

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
    protected void performEffect(ISpiritweb data)
    {
        int mode = data.getMode(manifestationType, getMetalType().getID());

        //todo, replace x * mode with config based value
        double allomanticStrength = getAllomanticStrength(data);

        int range = (int) (allomanticStrength * mode);

        List<LivingEntity> entitiesToAffect = getLivingEntitiesInRange(data.getLiving(), range, true);

        for (LivingEntity e : entitiesToAffect)
        {
            if (e instanceof MobEntity)
            {
                MobEntity mob = (MobEntity) e;
                mob.setNoAI(mode == 3 && allomanticStrength > 15);

                switch (mode)
                {
                    case 3:
                        mob.setAttackTarget(null);
                    case 2:
                        mob.setAggroed(false);
                    case 1:
                    default:
                        //stop angry targets from attacking things
                        e.setRevengeTarget(null);

                }
            }
        }
    }
}
