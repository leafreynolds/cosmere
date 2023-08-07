/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectsHelper
{
	public static MobEffectInstance getNewEffect(MobEffect effect, int amplifier)
	{
		MobEffectInstance effectInstance = new MobEffectInstance(
				effect,
				63,
				Math.max(0, amplifier),
				true, //usually means came from outside player means, eg beacon? if true, hides icon in non-inv gui
				false, // definitely don't want particles.
				true); // show icon though

		return effectInstance;
	}

	public static MobEffectInstance getNewEffect(MobEffect effect, int amplifier, int duration)
	{
		MobEffectInstance effectInstance = new MobEffectInstance(
				effect,
				duration,
				Math.max(0, amplifier),
				true, //usually means came from outside player means, eg beacon? if true, hides icon in non-inv gui
				false, // definitely don't want particles.
				true); // show icon though

		return effectInstance;
	}
}
