/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;

public class AllomancyTin extends AllomancyBase
{
    public AllomancyTin(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        //Increases Physical Senses

        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.ticksExisted % 20 == 0;
        //passive active ability, if any
        if (isActiveTick)
        {
            //give night vision
            livingEntity.addPotionEffect(EffectsHelper.getNewEffect(Effects.NIGHT_VISION, 0));
        }

        if (getKeyBinding().isPressed())
        {

        }


    }


}
