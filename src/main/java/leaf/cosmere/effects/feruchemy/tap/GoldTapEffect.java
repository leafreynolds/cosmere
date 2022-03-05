/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;


public class GoldTapEffect extends FeruchemyEffectBase
{
    public GoldTapEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
/*        addAttributesModifier(
                Attributes.MAX_HEALTH,
                "17a9094f-d300-46c4-8607-83f64a98bb42",
                4.0D,
                AttributeModifier.Operation.ADDITION);*/
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth())
        {
            entityLivingBaseIn.heal(amplifier + 1);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        int k = 40 >> amplifier;
        if (k > 0)
        {
            return duration % k == 0;
        }
        else
        {
            return true;
        }
    }
}
