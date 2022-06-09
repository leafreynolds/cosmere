package leaf.cosmere.mixin;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Inject(at = @At("RETURN"), method = "getNightVisionScale", cancellable = true)
	private static void removeNightVisionFlash(LivingEntity livingEntity, float f, CallbackInfoReturnable<Float> info)
	{
		MobEffectInstance effect = livingEntity.getEffect(MobEffects.NIGHT_VISION);
		float i = effect != null ? effect.getDuration() : f;
		info.setReturnValue(i > 0 ? 1 : i);
	}

	@Inject(at = @At("RETURN"),
			method = "getFov",
			cancellable = true
	)
	private void getZoomedFov(Camera activeRenderInfoIn, float partialTicks, boolean useFOVSetting, CallbackInfoReturnable<Double> info)
	{
		double fov = info.getReturnValue();
		Player player = (Player) Minecraft.getInstance().getCameraEntity();

		if (player == null)
		{
			return;
		}

		SpiritwebCapability.get(player).ifPresent(playerSpiritweb ->
		{
			final FeruchemyBase tinF = (FeruchemyBase) ManifestationRegistry.FERUCHEMY_POWERS.get(Metals.MetalType.TIN).get();

			if (tinF.isTapping(playerSpiritweb) && tinF.canAfford(playerSpiritweb, true))
			{
				final int i = Mth.abs(tinF.getMode(playerSpiritweb));
				final double pow = Math.pow(2, i);
				final double v = fov / pow;
				info.setReturnValue(v);
			}
		});
	}


	@Inject(at = @At("RETURN"),
			method = "getRenderDistance",
			cancellable = true
	)
	private void getRenderDistance(CallbackInfoReturnable<Float> info)
	{
		Player player = (Player) Minecraft.getInstance().getCameraEntity();

		if (player == null)
		{
			return;
		}

		SpiritwebCapability.get(player).ifPresent(playerSpiritweb ->
		{
			final FeruchemyBase feruchemy = (FeruchemyBase) ManifestationRegistry.FERUCHEMY_POWERS.get(Metals.MetalType.TIN).get();

			if (feruchemy.isStoring(playerSpiritweb) && feruchemy.canAfford(playerSpiritweb, true))
			{
				float renderDistance = info.getReturnValue();

				final int i = Mth.abs(feruchemy.getMode(playerSpiritweb));
				final double pow = Math.pow(2, i);
				final int floor = Mth.fastFloor(pow);
				final float v = renderDistance / floor;

				info.setReturnValue(v);
			}

		});


	}
}
