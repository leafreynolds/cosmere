/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.common.fog.FogManager;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

	@Inject(method = "tick()V", at = @At("TAIL"))
	private void tick(CallbackInfo info)
	{
		FogManager.getDensityManagerOptional().ifPresent((FogManager::tick));
	}
}
