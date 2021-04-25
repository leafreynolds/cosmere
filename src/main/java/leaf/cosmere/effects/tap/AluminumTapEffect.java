/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.potion.EffectType;

//identity tap
public class AluminumTapEffect extends FeruchemyEffectBase
{
    public AluminumTapEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
    }
}
