package leaf.cosmere.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class GuiUtils
{
	public static final Color BACKGROUND_COLOR = new Color(61, 61, 71);

	public static final int getInfoBoxWidth(Minecraft minecraft)
	{
		return (int) (minecraft.getWindow().getGuiScaledWidth() * 0.2);
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

	public record CachedQuad(float px, float py, float size) {}
}
