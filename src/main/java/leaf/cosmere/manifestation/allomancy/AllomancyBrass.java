/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

import java.util.List;

import static leaf.cosmere.helpers.EntityHelper.getEntitiesInRange;

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
        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.ticksExisted % 20 == 0;

        if (isActiveTick && getKeyBinding().isPressed())
        {

            int mode = data.getMode(manifestationType, getMetalType().getID());

            int range = 5 * mode;

            List<LivingEntity> entitiesToAffect = getEntitiesInRange(data.getLiving(), range, true);

            for (LivingEntity e : entitiesToAffect)
            {
                //stop angry targets from attacking things
                e.setRevengeTarget(null);

                if (livingEntity instanceof MobEntity)
                {
                    MobEntity mob = (MobEntity) livingEntity;
                    mob.setAggroed(false);
                    mob.setAttackTarget(null);
                }
            }

        }


    }


}
