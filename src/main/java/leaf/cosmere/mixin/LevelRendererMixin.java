/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.common.fog.FogManager;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// this mixin seems kinda unnecessary? --Gerbagel
@Mixin(LevelRenderer.class)
public class LevelRendererMixin
{
	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(CallbackInfo info)
	{
		FogManager.densityManager = new FogManager();

		CosmereAPI.logger.info("Allomancy Initialized Fog Density Manager");
	}

	@Inject(method = "close", at = @At("RETURN"))
	private void close(CallbackInfo info)
	{
		FogManager.getDensityManager().close();
		FogManager.densityManager = null;
		CosmereAPI.logger.info("Allomancy closed Fog Density Manager");
	}
}