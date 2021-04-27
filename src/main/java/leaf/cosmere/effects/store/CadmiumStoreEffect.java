/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;

// air
public class CadmiumStoreEffect extends FeruchemyEffectBase
{
    public CadmiumStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        //assume we can apply the effect regardless
        boolean result = true;

        int k = 50 >> amplifier;
        if (k > 0)
        {
            result = duration % k == 0;
        }

        return result;
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.world.isRemote || entityLivingBaseIn.ticksExisted % 20 != 0)
        {
            return;
        }

        //todo actually test this
        entityLivingBaseIn.setAir(entityLivingBaseIn.getAir() - amplifier);
    }
}
