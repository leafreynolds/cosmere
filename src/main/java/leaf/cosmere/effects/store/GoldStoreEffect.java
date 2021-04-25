/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;

//health
public class GoldStoreEffect extends FeruchemyEffectBase
{
    public GoldStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
        addAttributesModifier(
                Attributes.MAX_HEALTH,
                "2ee9153f-372f-4bd2-b21a-ccf08fecb8fa",
                -4.0D,
                AttributeModifier.Operation.ADDITION);
    }
}
