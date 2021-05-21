/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.MathHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

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
        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.ticksExisted % 20 == 0;

        if (isActiveTick)
        {
            int mode = data.getMode(manifestationType, getMetalType().getID());

            int range = 5 * mode;

            //include self in the list, mostly for setting self as a possible target to attack.
            List<LivingEntity> entitiesToAffect = getLivingEntitiesInRange(data.getLiving(), range, true);

            for (LivingEntity entityInRange : entitiesToAffect)
            {
                if (entityInRange instanceof MobEntity)
                {
                    MobEntity mob = (MobEntity) livingEntity;
                    mob.setAggroed(true);
                    if (mob.getAttackTarget() == null)
                    {
                        LivingEntity attackTarget = entitiesToAffect.get(MathHelper.randomInt(0, entitiesToAffect.size()));
                        entityInRange.setRevengeTarget(attackTarget);
                        mob.setAttackTarget(attackTarget);
                    }
                }
            }
        }
    }
}
