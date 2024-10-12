/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.fog;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.InterpolatedValue;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

// Based on Fog Looks Good Now by Birsy
// acquired on 04/11/2023 - in accordance with their CC0-1.0 license, which allows for use to take and modify code.
// Fantastic mod, very straightforward to understand and modify, so big thanks to them :)
//https://github.com/birsy/Fog-Looks-Good-Now/tree/main
public class FogManager
{
	@Nullable
	public static FogManager densityManager;

	public static FogManager getDensityManager()
	{
		return Objects.requireNonNull(densityManager, "Attempted to call getDensityManager before it finished loading!");
	}

	public static Optional<FogManager> getDensityManagerOptional()
	{
		return Optional.ofNullable(densityManager);
	}

	private final Minecraft mc;
	public InterpolatedValue fogStart;
	public InterpolatedValue fogEnd;

	public Vec3 unlitFogColor = Vec3.ZERO;

	public FogManager()
	{
		this.mc = Minecraft.getInstance();
		this.fogStart = new InterpolatedValue(0.9F);
		this.fogEnd = new InterpolatedValue(1.0F);
	}

	public void tick()
	{
		BlockPos pos = this.mc.gameRenderer.getMainCamera().getBlockPosition();

		if (this.mc.level == null)
		{
			return;
		}
		ClientLevel pLevel = this.mc.level;
		boolean isFogDense = pLevel.effects().isFoggyAt(pos.getX(), pos.getZ()) || this.mc.gui.getBossOverlay().shouldCreateWorldFog();
		float density = isFogDense ? 0.9F : 1.0F;

		Camera camera = this.mc.gameRenderer.getMainCamera();
		BiomeManager biomemanager = pLevel.getBiomeManager();
		Vec3 playerPos = camera.getPosition().subtract(2.0D, 2.0D, 2.0D).scale(0.25D);
		this.unlitFogColor = CubicSampler.gaussianSampleVec3(
				playerPos,
				(p_109033_, p_109034_, p_109035_) ->
						pLevel.effects().getBrightnessDependentFogColor(
								Vec3.fromRGB24(
										biomemanager.getNoiseBiomeAtQuart(p_109033_, p_109034_, p_109035_)
												.value()
												.getFogColor()
								),
								1)
		);

		getDarknessEffectedFog(this.fogStart.defaultValue, this.fogEnd.defaultValue * density);

	}

	public void getDarknessEffectedFog(float fs, float fd)
	{
		float renderDistance = mc.gameRenderer.getRenderDistance() * 16;

		Entity entity = mc.cameraEntity;
		float fogStart = fs;
		float fogEnd = fd;
		this.fogEnd.setInterpolationSpeed(0.05f);
		this.fogStart.setInterpolationSpeed(0.05f);
		if (entity instanceof LivingEntity e)
		{
			if (e.hasEffect(MobEffects.BLINDNESS))
			{
				fogStart = (4 * 16) / renderDistance;
				fogEnd = (8 * 16) / renderDistance;
			}
			else if (e.hasEffect(MobEffects.DARKNESS))
			{
				MobEffectInstance effect = e.getEffect(MobEffects.DARKNESS);
				if (!effect.getFactorData().isEmpty())
				{
					float factor = this.mc.options.darknessEffectScale().get().floatValue();
					float intensity = effect.getFactorData().get().getFactor(e, mc.getPartialTick()) * factor;
					float darkness = 1 - (calculateDarknessScale(e, effect.getFactorData().get().getFactor(e, mc.getPartialTick()), mc.getPartialTick()));
					CosmereAPI.logger.info("FogManager darkness intensity: " + intensity);
					fogStart = ((8.0F * 16) / renderDistance) * darkness;
					fogEnd = ((15.0F * 16) / renderDistance);
				}
			}
		}
		this.fogStart.interpolate(fogStart);
		this.fogEnd.interpolate(fogEnd);
	}

	private float calculateDarknessScale(LivingEntity pEntity, float darknessFactor, float partialTicks)
	{
		float factor = this.mc.options.darknessEffectScale().get().floatValue();
		float f = 0.45F * darknessFactor;
		return Math.max(0.0F, Mth.cos(((float) pEntity.tickCount - partialTicks) * (float) Math.PI * 0.025F) * f) * factor;
	}

	public void close()
	{
	}

	public static void resetFog()
	{
		if (densityManager != null)
		{
			densityManager.fogStart.setDefaultValue(0.9F);
			densityManager.fogEnd.setDefaultValue(1.0F);
		}
	}
}
