/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;

import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyCadmium extends AllomancyBase
{
    public AllomancyCadmium(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.ticksExisted % 20 == 0;

        //Slows Down Time
        if (isActiveTick)
        {
            int mode = data.getMode(manifestationType, getMetalType().getID());

            int range = 5 * mode;

            List<LivingEntity> entitiesToAffect = getLivingEntitiesInRange(data.getLiving(), range, true);

            for (LivingEntity e : entitiesToAffect)
            {
                e.addPotionEffect(EffectsHelper.getNewEffect(Effects.SLOWNESS,mode));
            }

            //todo slow tile entities? not sure how to do that. bendalloy just calls tick more often.
        }
    }
}
