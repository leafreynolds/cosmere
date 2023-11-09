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

@Mixin(LevelRenderer.class)
public class LevelRendererMixin
{
	@Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;Lnet/minecraft/client/renderer/RenderBuffers;)V", at = @At("TAIL"))
	private void init(CallbackInfo info)
	{
		FogManager.densityManager = new FogManager();

		CosmereAPI.logger.info("Allomancy Initialized Fog Density Manager");
	}

	@Inject(method = "close()V", at = @At("TAIL"))
	private void close(CallbackInfo info)
	{
		FogManager.getDensityManager().close();
		FogManager.densityManager = null;
		CosmereAPI.logger.info("Allomancy closed Fog Density Manager");
	}
}