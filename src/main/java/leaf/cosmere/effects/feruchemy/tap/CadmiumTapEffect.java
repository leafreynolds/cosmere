/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.MathHelper;


public class CadmiumTapEffect extends FeruchemyEffectBase
{
    public CadmiumTapEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.level.isClientSide)
        {
            return;
        }

        entityLivingBaseIn.setAirSupply(MathHelper.clamp(entityLivingBaseIn.getAirSupply() + 3 + (amplifier), entityLivingBaseIn.getAirSupply(), entityLivingBaseIn.getMaxAirSupply()));

    }
}
