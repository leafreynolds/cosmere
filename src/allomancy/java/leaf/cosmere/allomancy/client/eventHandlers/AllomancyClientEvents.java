/*
 * File updated ~ 5 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.eventHandlers;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.allomancy.client.fog.FogManager;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.capabilities.world.IScadrial;
import leaf.cosmere.allomancy.common.capabilities.world.ScadrialCapability;
import leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel;
import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Allomancy.MODID, value = Dist.CLIENT)
public class AllomancyClientEvents
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onRecipesUpdated(final RecipesUpdatedEvent event)
	{
		AllomancyIronSteel.invalidateWhitelist();
	}

	@SubscribeEvent
	public static void onRenderFog(ViewportEvent.RenderFog event)
	{
		final Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		Level level = mc.level;

		//check dimension -
		if (player == null || event.getCamera().getFluidInCamera() != FogType.NONE)
		{
			return;
		}

		//check capability
		// todo - check this only happens to whatever dimension scadrial is attached to. currently overworld.
		LazyOptional<IScadrial> cap = ScadrialCapability.get(level);
		final Optional<IScadrial> resolve = cap.resolve();

		if (!cap.isPresent() || resolve.isEmpty())
		{
			return;
		}

		//must exist, lets get it done
		ScadrialCapability scadrial = (ScadrialCapability) resolve.get();

		//once again, special thank you to the fog looks good now team - it helped a lot without requiring another dependency
		//https://github.com/birsy/Fog-Looks-Good-Now/blob/main/src/main/java/birsy/foglooksgoodnow/client/ClientEvents.java
		FogManager densityManager = FogManager.getDensityManager();

		//this checks time of day, don't bring the mists in until it's night
		//near plane moves first
		final float mistNearDistance = scadrial.getMistNearDistance();
		//far plane moves after that
		final float mistFarDistance = scadrial.getMistFarDistance();


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
