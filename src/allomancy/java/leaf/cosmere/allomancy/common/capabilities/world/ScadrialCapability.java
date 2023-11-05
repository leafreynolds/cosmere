/*
 * File updated ~ 5 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.capabilities.world;

import leaf.cosmere.api.math.Easing;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class ScadrialCapability implements IScadrial
{
	//Injection
	public static final Capability<IScadrial> CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});

	Level level;

	CompoundTag nbt = null;

	public ScadrialCapability(Level level)
	{
		this.level = level;
	}

	@Nonnull
	public static LazyOptional<IScadrial> get(Level level)
	{
		return level != null ? level.getCapability(ScadrialCapability.CAPABILITY, null)
		                     : LazyOptional.empty();
	}


	@Override
	public CompoundTag serializeNBT()
	{
		if (nbt == null)
		{
			nbt = new CompoundTag();
		}

		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.nbt = nbt;
	}

	@Override
	public float getMistNearDistance()
	{

		final long timeOfDay = level.getDayTime() % 24000L;

		final float easeTime = 3500;
		final float fogInStartTime = 11500;
		final float fogInEndTime = fogInStartTime + easeTime;
		final float fogOutStartTime = 20499;
		final float fogOutEndTime = fogOutStartTime + easeTime;

		if (timeOfDay <= fogInStartTime || timeOfDay >= fogOutEndTime)
		{
			return 0.9f;//basically put the fog as far away as possible
		}

		float i;

		//if time is more than start time, and less than end time
		if (timeOfDay < fogInEndTime)
		{
			final float percentageThroughFogTransition = (timeOfDay - fogInStartTime) / easeTime;
			i = Easing.easeOutQuad(percentageThroughFogTransition);
		}
		//if time is after fog in, and before fog out
		//basically the default state for The Mists surrounding the player
		else if (timeOfDay < fogOutStartTime)
		{
			//max strength?
			//todo config? do we even want people to have control over it?
			//this means it should be as close as it can possibly be.
			return 0f;
		}
		else // start decreasing mists level
		{
			final float percentageThroughFogTransition = (timeOfDay - fogOutStartTime) / easeTime;
			//invert the value so that the mists ease away
			i = 1 - Easing.easeInQuad(percentageThroughFogTransition);
		}


		//1 means it's pushed back as far as possible
		//0.1 means it's as close as it can be
		return (float) Mth.lerp(i, 0.9f, 0.0f);
	}

	@Override
	public float getMistFarDistance()
	{

		final long timeOfDay = level.getDayTime() % 24000L;

		final float easeTime = 3500;
		final float fogInStartTime = 12500;
		final float fogInEndTime = fogInStartTime + easeTime;
		final float fogOutStartTime = 19500;

		if (timeOfDay <= fogInStartTime)
		{
			return 1;//basically put the fog as far away as possible
		}

		float i;

		//if time is more than start time, and less than end time
		if (timeOfDay < fogInEndTime)
		{
			final float percentageThroughFogTransition = (timeOfDay - fogInStartTime) / easeTime;
			i = Easing.easeOutQuad(percentageThroughFogTransition);
		}
		//if time is after fog in, and before fog out
		//basically the default state for The Mists surrounding the player
		else if (timeOfDay < fogOutStartTime)
		{
			//max strength?
			//todo config? do we even want people to have control over it?
			//this means it should be as close as it can possibly be.
			return 0.1f;
		}
		else // start decreasing mists level
		{
			final float percentageThroughFogTransition = (timeOfDay - fogOutStartTime) / easeTime;
			//invert the value so that the mists ease away
			i = 1 - Easing.easeInQuad(percentageThroughFogTransition);
		}


		//1 means it's pushed back as far as possible
		//0.1 means it's as close as it can be
		return (float) Mth.lerp(i, 1f, 0.1f);
	}
}
