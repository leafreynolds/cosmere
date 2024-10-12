/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.client;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class HUDHandler
{
	private static final ResourceLocation stormlightBar = new ResourceLocation(Surgebinding.MODID, "textures/gui/stormlight_hud.png");


	public static void onDrawScreenPost(GuiGraphics guiGraphics)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.options.hideGui)
		{
			return;
		}
		ProfilerFiller profiler = mc.getProfiler();
		Player player = mc.player;
		if (player == null)
		{
			return;
		}

		profiler.push("stormlight-hud");
		{
			SpiritwebCapability.get(mc.player).ifPresent(spiritweb ->
			{
				SpiritwebCapability data = (SpiritwebCapability) spiritweb;

				profiler.push("stormlight-bar");
				{
					if (!player.isSpectator())
					{
						var sbModule = (SurgebindingSpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

						final int heldStormlight = sbModule.getStormlight();
						if (heldStormlight > 0)
						{
							final int maxPlayerStormlight = SurgebindingConfigs.SERVER.PLAYER_MAX_STORMLIGHT.get();
							renderStormlightBar(guiGraphics, heldStormlight, maxPlayerStormlight);
						}
					}

				}
				profiler.pop();
			});
		}
		profiler.pop();

		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
	}

	private static void renderStormlightBar(GuiGraphics gui, int heldStormlight, int maxPlayerStormlight)
	{
		Minecraft mc = Minecraft.getInstance();
		int barWidth = 182;
		int barStartingX = mc.getWindow().getGuiScaledWidth() / 2 - barWidth / 2;

		//todo stormlight bar height config
		final int stormlightBarHeight = 32;

		int barStartingY = mc.getWindow().getGuiScaledHeight() - stormlightBarHeight;

		if (maxPlayerStormlight == 0)
		{
			barWidth = 0;
		}
		else
		{
			barWidth *= (double) heldStormlight / (double) maxPlayerStormlight;
		}

		if (barWidth == 0)
		{
			if (heldStormlight > 0)
			{
				barWidth = 1;
			}
			else
			{
				return;
			}
		}

		final float hue = 0.55F;
		//add/remove saturation? Currently, it oscillates between blue and white
		final float saturation = (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 200D) * 0.5 + 1F);
		final float brightness = 1F;

		final int rgb = Color.HSBtoRGB(hue, saturation, brightness);
		Color color = new Color(rgb);

		//todo get color of order by gem
		final int red = color.getRed();
		final int green = color.getGreen();
		final int blue = color.getBlue();

		RenderSystem.setShaderColor(
				red / 255F,
				green / 255F,
				blue / 255F,
				1// - (red / 255F)//fading in and out?
		);

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//draw textured rect
		gui.blit(stormlightBar, barStartingX, barStartingY, 0, 251, barWidth, 5, 256, 256);

		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
