/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.helpers;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class EffectsHelper
{


    public static EffectInstance getNewEffect(Effect effect, int amplifier)
    {
        EffectInstance effectInstance = new EffectInstance(
                effect,
                21,
                Math.max(0, amplifier),
                false, //usually means came from outside player means, eg beacon? if true, hides icon in non-inv gui
                false, // definitely don't want particles.
                true); // show icon though

        return effectInstance;
    }
}
