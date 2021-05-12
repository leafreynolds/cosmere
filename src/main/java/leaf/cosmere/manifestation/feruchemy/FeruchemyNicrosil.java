/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.MetalmindChargeHelper;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;


//storing all the available powers on the user individually
public class FeruchemyNicrosil extends FeruchemyBase
{
    public FeruchemyNicrosil(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    public int modeMin(ISpiritweb data)
    {
        return -1;
    }

    @Override
    public int modeMax(ISpiritweb data)
    {
        return 1;
    }

    @Override
    public void tick(ISpiritweb data)
    {

        //don't check every tick.
        LivingEntity livingEntity = data.getLiving();

        if (livingEntity.ticksExisted % 20 != 0)
        {
            return;
        }

        int mode = data.getMode(manifestationType, metalType.getID());

        int cost;

        Effect effect = getEffect(mode);

        // if we are tapping
        //check if there is charges to tap
        if (mode < 0)
        {
            //wanting to tap
            //get cost
            cost = mode <= -3 ? -(mode * mode) : mode;

        }
        //if we are storing
        //check if there is space to store
        else if (mode > 0)
        {
            cost = mode;
        }
        //can't store or tap any more
        else
        {
            //remove active effects.
            //let the current effect run out.
            return;
        }

        if (MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, -cost, true, true))
        {
            EffectInstance currentEffect = EffectsHelper.getNewEffect(effect, Math.abs(mode) - 1);
            livingEntity.addPotionEffect(currentEffect);
        }

    }

}
