/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.capabilities.world;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.math.Easing;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.fog.FogManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
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

	@Override
	@OnlyIn(Dist.CLIENT)
	public void tickFog(ViewportEvent.RenderFog event, Player player)
	{
		//once again, special thank you to the fog looks good now team - it helped a lot without requiring another dependency
		//https://github.com/birsy/Fog-Looks-Good-Now/blob/main/src/main/java/birsy/foglooksgoodnow/client/ClientEvents.java
		FogManager densityManager = FogManager.getDensityManager();

		//this checks time of day, don't bring the mists in until it's night
		//near plane moves first
		final float mistNearDistance = this.getMistNearDistance();
		//far plane moves after that
		final float mistFarDistance = this.getMistFarDistance();


		//if it's nighttime, we want to make the mists more dense
		//except when they are burning tin, in which case we want to make them less dense right out to render distance
		//check burning tin, if it even exists
		float tinAlloVal = 0;
		{
			final LazyOptional<ISpiritweb> iSpiritwebLazyOptional = SpiritwebCapability.get(player);

			if (iSpiritwebLazyOptional.isPresent())
			{
				var spiritweb = iSpiritwebLazyOptional.resolve();
				final AllomancyManifestation tinAllomancy = AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.TIN).get();
				//if tin allomancy exists in this mod pack and it's currently active
				if (spiritweb.isPresent() && spiritweb.get() instanceof SpiritwebCapability data && tinAllomancy != null && tinAllomancy.isMetalBurning(data))
				{
					//burning or flaring strength
					double currentBurnStrength = tinAllomancy.getStrength(data, false) * data.getMode(tinAllomancy);
					final RangedAttribute attribute = (RangedAttribute) tinAllomancy.getAttribute();
					double maxTinFlareStrengthPossible = attribute.getMaxValue() * 2;

					//tin takes actual strength into account, as compared to total possible strength including flaring.
					//todo - move the min/max night vision to config, ideally server side that gets synced to client
					tinAlloVal = (float) Mth.lerp(currentBurnStrength / maxTinFlareStrengthPossible, 0.0f, 0.95f);
				}
			}
		}

		//a low mist distance means it is closer, so we add tin allo value to it to push it further away.
		float startFogDistance = Mth.clamp(mistNearDistance + tinAlloVal, 0.0f, 1.0f);
		float endFogDistance = Mth.clamp(mistFarDistance + tinAlloVal, 0.0f, 1.0f);

		//that said, we are probably never going to otherwise want it to be less than 0.1 at night.
		densityManager.fogStart.setDefaultValue(startFogDistance);
		densityManager.fogEnd.setDefaultValue(endFogDistance);

		float renderDistance = event.getRenderer().getRenderDistance();

		// render distance matters a bit here, obviously beefier computers will have a more pronounced effect.
		// hopefully not too bad on low render distance, but there may not be much we can do about it.
		final float pShaderFogStart = renderDistance * densityManager.fogStart.get((float) event.getPartialTick());
		final float pShaderFogEnd = renderDistance * densityManager.fogEnd.get((float) event.getPartialTick());

		//but end fog is what actually matters
		//if fog is at 1, it's as far away as it can be
		//if fog is at 0, they're basically blind,
		// which todo - maybe we can transition to using that instead of blindness, just means moving this to cosmere instead of allomancy
		RenderSystem.setShaderFogStart(pShaderFogStart);
		RenderSystem.setShaderFogEnd(pShaderFogEnd);

		//should probably test cylinder at some point.
		RenderSystem.setShaderFogShape(FogShape.SPHERE);
	}
}
