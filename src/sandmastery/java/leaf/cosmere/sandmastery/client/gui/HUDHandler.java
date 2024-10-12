/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class HUDHandler
{
	private static final ResourceLocation hydrationBar = new ResourceLocation(Sandmastery.MODID, "textures/gui/hydration_hud.png");

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

		profiler.push("sandmastery-hud");
		{
			SpiritwebCapability.get(mc.player).ifPresent(spiritweb ->
			{
				SpiritwebCapability data = (SpiritwebCapability) spiritweb;

				profiler.push("hydration-bar");
				{
					if (!player.isSpectator() && !player.isCreative())
					{
						var sbModule = (SandmasterySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);

						final int hydrationLevel = sbModule.getHydrationLevel();
						if (hydrationLevel > 0)
						{
							renderHydrationBar(guiGraphics, hydrationLevel, SandmasteryConfigs.SERVER.MAX_HYDRATION.get());
						}
					}

				}
				profiler.pop();
			});
		}
		profiler.pop();

		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
	}

	private static void renderHydrationBar(GuiGraphics gui, int hydration, int maxHydration)
	{
		Minecraft mc = Minecraft.getInstance();
		int fullBarWidth = 83;
		int barWidth;
		int barStartingX = (mc.getWindow().getGuiScaledWidth() / 2 - fullBarWidth / 2) + 50;

		//todo hydration bar height config
		final int offsetFromBottom = 28;
		final int hydrationBarHeight = 11;

		int barStartingY = mc.getWindow().getGuiScaledHeight() - (hydrationBarHeight + offsetFromBottom);

		if (maxHydration == 0)
		{
			barWidth = 0;
		}
		else
		{
			double multiplier = (double) hydration / (double) maxHydration;
			barWidth = (int) ((double) fullBarWidth * multiplier);
		}

		if (barWidth == 0)
		{
			if (hydration > 0)
			{
				barWidth = 1;
			}
			else
			{
				return;
			}
		}

		final float hue = 0.55F;
		final float saturation = 1F;
		final float brightness = 1F;

		final int rgb = Color.HSBtoRGB(hue, saturation, brightness);
		Color color = new Color(rgb);

		final int red = color.getRed();
		final int green = color.getGreen();
		final int blue = color.getBlue();

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(
				red / 255F,
				green / 255F,
				blue / 255F,
				0.25F
		);
		//draw textured rect
		gui.blit(
				hydrationBar,
				barStartingX,
				barStartingY,
				0,
				0,
				fullBarWidth,
				hydrationBarHeight,
				fullBarWidth,
				hydrationBarHeight);

		RenderSystem.setShaderColor(
				red / 255F,
				green / 255F,
				blue / 255F,
				1
		);

		gui.blit(
				hydrationBar,
				barStartingX,
				barStartingY,
				0,
				0,
				barWidth,
				hydrationBarHeight,
				fullBarWidth,
				hydrationBarHeight);

		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
