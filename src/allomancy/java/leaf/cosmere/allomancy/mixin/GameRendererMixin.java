/*
 * File updated ~ 4 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.mixin;

import leaf.cosmere.allomancy.client.fog.FogManager;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Inject(method = "tick()V", at = @At("TAIL"))
	private void tick(CallbackInfo info)
	{
		FogManager.getDensityManagerOptional().ifPresent((FogManager::tick));
	}
}