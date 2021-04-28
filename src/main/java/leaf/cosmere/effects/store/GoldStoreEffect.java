/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

//health
public class GoldStoreEffect extends FeruchemyEffectBase
{
    public GoldStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
/*        addAttributesModifier(
                Attributes.MAX_HEALTH,
                "2ee9153f-372f-4bd2-b21a-ccf08fecb8fa",
                -4.0D,
                AttributeModifier.Operation.ADDITION);*/
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        if (entityLivingBaseIn.getHealth() > (entityLivingBaseIn.getMaxHealth() / amplifier + 2))
        {
            entityLivingBaseIn.setHealth(entityLivingBaseIn.getHealth() - 1);
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier)
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
