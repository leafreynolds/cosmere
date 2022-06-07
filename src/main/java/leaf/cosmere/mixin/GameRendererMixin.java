package leaf.cosmere.mixin;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
		MobEffectInstance tinTapEffect = player.getEffect(EffectsRegistry.TAPPING_EFFECTS.get(Metals.MetalType.TIN).get());
		if (tinTapEffect != null && tinTapEffect.getDuration() > 0)
		{
			int amplifier = tinTapEffect.getAmplifier();
			double zoomedFov = fov / (amplifier + 2);
			info.setReturnValue(zoomedFov);
			return;
		}

		info.setReturnValue(fov);
	}


	@Inject(at = @At("RETURN"),
			method = "getRenderDistance",
			cancellable = true
	)
	private void getRenderDistance(CallbackInfoReturnable<Float> info)
	{
		float renderDistance = info.getReturnValue();
		Player player = (Player) Minecraft.getInstance().getCameraEntity();
		MobEffectInstance tinTapEffect = null;
		if (player != null)
		{
			tinTapEffect = player.getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.TIN).get());
		}
		if (tinTapEffect != null && tinTapEffect.getDuration() > 0)
		{
			int amplifier = tinTapEffect.getAmplifier();
			final int i = amplifier + 1;
			final double pow = Math.pow(2, i);
			final int floor = Mth.fastFloor(pow);
			final float v = renderDistance / floor;

			info.setReturnValue(v);
		}

	}
}
