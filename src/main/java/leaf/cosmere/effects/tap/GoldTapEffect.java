/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;


public class GoldTapEffect extends FeruchemyEffectBase
{
    public GoldTapEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
        addAttributesModifier(
                Attributes.MAX_HEALTH,
                "17a9094f-d300-46c4-8607-83f64a98bb42",
                4.0D,
                AttributeModifier.Operation.ADDITION);
    }
}
