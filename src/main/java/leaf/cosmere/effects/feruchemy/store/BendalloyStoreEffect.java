/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;

// food
public class BendalloyStoreEffect extends FeruchemyEffectBase
{
    public BendalloyStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
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
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (!entityLivingBaseIn.level.isClientSide)
        {
            ((PlayerEntity) entityLivingBaseIn).getFoodData().addExhaustion(0.5F * (float) (amplifier + 1));

            //todo add tough as nails mod compatibility?
        }
    }
}
