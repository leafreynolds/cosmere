/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;


public class PewterTapEffect extends FeruchemyEffectBase
{
    public PewterTapEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);

        addAttributesModifier(
                Attributes.ATTACK_DAMAGE,
                "00bebe52-fe9e-4966-989c-28de0cd2eb1f",
                1.0D,
                AttributeModifier.Operation.ADDITION);
        addAttributesModifier(
                Attributes.ATTACK_KNOCKBACK,
                "74b5b82b-58e0-4a34-9cc2-fd4b92f0b11b",
                1.0D,
                AttributeModifier.Operation.ADDITION);
    }
}
