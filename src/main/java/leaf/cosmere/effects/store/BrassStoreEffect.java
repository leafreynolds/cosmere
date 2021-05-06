/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import leaf.cosmere.helpers.EffectsHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

//warmth
public class BrassStoreEffect extends FeruchemyEffectBase
{
    public BrassStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        //ensure the user has fire resistence at least as strong as their store effect

        if (entityLivingBaseIn.world.isRemote || entityLivingBaseIn.ticksExisted % 20 != 0)
        {
            return;
        }
        entityLivingBaseIn.addPotionEffect(EffectsHelper.getNewEffect(Effects.FIRE_RESISTANCE, amplifier));
    }
}
