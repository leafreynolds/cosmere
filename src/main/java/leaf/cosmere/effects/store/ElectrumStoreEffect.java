/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.FeruchemyEffectBase;
import net.minecraft.potion.EffectType;

//determination. todo decide what the heck it should do
//storing determination puts you in a depressed/apathetic state
public class ElectrumStoreEffect extends FeruchemyEffectBase
{
    public ElectrumStoreEffect(Metals.MetalType type, EffectType effectType)
    {
        super(type, effectType);
    }
}
