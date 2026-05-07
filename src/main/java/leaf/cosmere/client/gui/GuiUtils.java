package leaf.cosmere.client.gui;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;

import java.awt.*;

public class GuiUtils
{
	public static final Color POSITIVE_USE_COLOR = new Color(255, 0, 0);
	public static final Color NEGATIVE_USE_COLOR = new Color(0, 0, 255);
	public static final Color BACKGROUND_COLOR = new Color(61, 61, 71);

	public static final int getInfoBoxWidth(Minecraft minecraft)
	{
		int maxFontWidth = minecraft.font.width("Feruchemical Duralumin -16"); // widest possible string in English; probably needs a proper translation string
		return (int) (maxFontWidth*0.8) + 10; // 0.8 because that's the scaling we use in UI elements, and + 10 so it's not jammed up to the edge
	}

	public static final int getInfoBoxHeight(Minecraft minecraft)
	{
		return minecraft.getWindow().getGuiScaledHeight() / 8;
	}

	public static void drawScaledString(Font font, GuiGraphics pGuiGraphics, String text, float x, float y, float scale, int color)
	{
		pGuiGraphics.pose().pushPose();
		pGuiGraphics.pose().translate(x, y, 0);
		pGuiGraphics.pose().scale(scale, scale, 1.0f);
		pGuiGraphics.drawString(font, text, 0, 0, color, false);
		pGuiGraphics.pose().popPose();
	}

	public static Color shiftColor(Color input, float intensity, Color target)
	{
		float r = input.getRed()/256.f, g = input.getGreen()/256.f, b = input.getBlue()/256.f;
		float tR = target.getRed()/256.f, tG = target.getGreen()/256.f, tB = target.getBlue()/256.f;

		r = lerp(r, tR, intensity);
		g = lerp(g, tG, intensity);
		b = lerp(b, tB, intensity);

		return new Color(r, g, b);
	}

	public static float lerp(float start, float end, float pct) {
		return start + pct * (end - start);
	}

	public record CachedQuad(float px, float py, float size) {}
}
