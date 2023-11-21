/*
 * File updated ~ 20 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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

	public static CosmereEffectInstance getNewEffect(CosmereEffect effect, Entity effectSource, double strength)
	{
		return getNewEffect(effect, effectSource, strength, 93);
	}

	public static CosmereEffectInstance getNewEffect(CosmereEffect effect, Entity effectSource, double strength, int duration)
	{
		//generate uuid based on the effect, and the source it comes from
		//surely nothing will go wrong with this, right? then we can have multiple effects from the same source entity
		UUID uuid = getEffectUUID(effect, effectSource);
		return getNewEffect(effect, uuid, strength, duration);
	}

	@NotNull
	public static UUID getEffectUUID(CosmereEffect effect, Entity effectSource)
	{
		return getEffectUUID(effect, effectSource.getStringUUID());
	}

	@NotNull
	public static UUID getEffectUUID(CosmereEffect effect, String uuidSource)
	{
		String sourceAndEffect = uuidSource + effect.getRegistryName();
		UUID uuid = UUID.nameUUIDFromBytes(sourceAndEffect.getBytes());
		return uuid;
	}

	public static CosmereEffectInstance getNewEffect(CosmereEffect effect, UUID uuid, double strength, int duration)
	{
		CosmereEffectInstance effectInstance = new CosmereEffectInstance(
				effect,
				uuid,
				Math.max(0, strength),
				duration);

		return effectInstance;
	}
}
