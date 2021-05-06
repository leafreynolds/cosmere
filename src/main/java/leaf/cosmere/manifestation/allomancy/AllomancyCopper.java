/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.helpers.EffectsHelper;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

import java.util.List;

import static leaf.cosmere.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyCopper extends AllomancyBase
{
    public AllomancyCopper(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.ticksExisted % 20 == 0;
        //Hides Allomantic Pulses

        //passive active ability, if any
        if (isActiveTick)
        {
            int mode = data.getMode(manifestationType, getMetalType().getID());
            EffectInstance newEffect = EffectsHelper.getNewEffect(EffectsRegistry.ALLOMANTIC_COPPER.get(), mode - 1);


            //data.getLiving().addPotionEffect(newEffect);

            List<LivingEntity> entitiesToApplyEffect = getLivingEntitiesInRange(livingEntity, 5, true);

            for (LivingEntity e : entitiesToApplyEffect)
            {
                e.addPotionEffect(newEffect);
            }
        }

        if (getKeyBinding().isPressed())
        {

        }


    }


}
