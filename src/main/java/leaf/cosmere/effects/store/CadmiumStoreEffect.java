/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

// air
public class CadmiumStoreEffect extends FeruchemyEffectBase
{
    public CadmiumStoreEffect(Metals.MetalType type, EffectType effectType)
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

        entityLivingBaseIn.setAir(MathHelper.clamp(entityLivingBaseIn.getAir() - 4 - (amplifier), -20, entityLivingBaseIn.getMaxAir()));

        if (entityLivingBaseIn.getAir() < -10 && entityLivingBaseIn.ticksExisted % 50 == 0)
        {
            entityLivingBaseIn.attackEntityFrom(DamageSource.DROWN, 2.0F);
        }
    }
}
