/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.mixin;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import leaf.cosmere.feruchemy.common.registries.FeruchemyManifestations;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{

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
			final FeruchemyManifestation tinF = (FeruchemyManifestation) FeruchemyManifestations.FERUCHEMY_POWERS.get(Metals.MetalType.TIN).get();

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
			final FeruchemyManifestation feruchemy = (FeruchemyManifestation) FeruchemyManifestations.FERUCHEMY_POWERS.get(Metals.MetalType.TIN).get();

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
