/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
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
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.world.isRemote)
        {
            return;
        }

        entityLivingBaseIn.setAir(MathHelper.clamp(entityLivingBaseIn.getAir() + 3 + (amplifier), entityLivingBaseIn.getAir(), entityLivingBaseIn.getMaxAir()));

    }
}
