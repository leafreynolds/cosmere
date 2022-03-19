/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;


public class DuraluminTapEffect extends FeruchemyEffectBase
{
    public DuraluminTapEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
    {
        //ensure the user has correct buffs at least as strong as their store effect
        if (entityLivingBaseIn.level.isClientSide || entityLivingBaseIn.tickCount % 20 != 0)
        {
            return;
        }

        entityLivingBaseIn.addEffect(EffectsHelper.getNewEffect(Effects.HERO_OF_THE_VILLAGE, amplifier));
    }
}
