/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;


public class PewterStoreEffect extends FeruchemyEffectBase
{
    public PewterStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
        addAttributesModifier(
                Attributes.ATTACK_DAMAGE,
                "e8f5c2b2-f724-4514-82b8-9d10be304c42",
                -1.0D,
                AttributeModifier.Operation.ADDITION);
        addAttributesModifier(
                Attributes.ATTACK_KNOCKBACK,
                "cb83254e-f9c4-4d67-9976-f2fdf69061ce",
                -1.0D,
                AttributeModifier.Operation.ADDITION);
    }
}
