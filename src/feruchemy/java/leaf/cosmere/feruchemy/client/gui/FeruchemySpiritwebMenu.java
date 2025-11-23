package leaf.cosmere.feruchemy.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FeruchemySpiritwebMenu extends Screen
{
	public FeruchemySpiritwebMenu()
	{
		super(Component.literal("Feruchemy"));
	}

	@Override
	protected void init()
	{

	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, "feruchemy", pMouseX, pMouseY, 0xFFFFFFFF);
	}
}
