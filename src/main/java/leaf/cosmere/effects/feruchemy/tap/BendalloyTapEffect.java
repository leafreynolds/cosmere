/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;


public class BendalloyTapEffect extends FeruchemyEffectBase
{
    public BendalloyTapEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
    }


    @Override
    public boolean isReady(int duration, int amplifier)
    {
        //assume we can apply the effect regardless
        boolean result = true;

        int k = 50 >> amplifier + 1;
        if (k > 0)
        {
            result = duration % k == 0;
        }

        return result;
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (!entityLivingBaseIn.world.isRemote)
        {
            ((PlayerEntity) entityLivingBaseIn).getFoodStats().addStats(amplifier + 1, 0.0F);

            //todo add tough as nails mod compatibility?
        }
    }
}
