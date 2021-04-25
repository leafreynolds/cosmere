/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class FeruchemyEffectBase extends Effect implements IHasMetalType
{
    protected final Metals.MetalType metalType;
    final double bonusPerLevel = 1;

    public FeruchemyEffectBase(Metals.MetalType type, EffectType effectType)
    {
        super(effectType, type.getColorValue());
        metalType = type;
    }

    @Override
    public Metals.MetalType getMetalType()
    {
        return this.metalType;
    }


    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.world.isRemote)
        {
            //client side only?
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        //assume we can apply the effect regardless
        boolean result = true;
/*
        //but if we are a specific effect
        if (metalType == Metals.MetalType.BENDALLOY)
        {
            int k = 50 >> amplifier;
            if (k > 0)
            {
                result = duration % k == 0;
            }
        }*/
        return result;
    }
}
