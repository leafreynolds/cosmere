/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;

public class AllomancyDuralumin extends AllomancyBase
{
    public AllomancyDuralumin(Metals.MetalType metalType)
    {
        super(metalType);
    }

    //Enhances Current Metals Burned
    @Override
    public void performEffect(ISpiritweb data)
    {
        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.tickCount % 20 == 0;

        if (isActiveTick)
        {
            //apply the effect regardless, because duralumin is currently active.
            EffectInstance newEffect = EffectsHelper.getNewEffect(
                    EffectsRegistry.ALLOMANCY_BOOST.get(),
                    MathHelper.floor(getStrength(data))
            );
            data.getLiving().addEffect(newEffect);
        }
    }
}
