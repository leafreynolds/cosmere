package leaf.cosmere.feruchemy.client.gui;

import leaf.cosmere.client.gui.CosmereScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class FeruchemySpiritwebMenu extends CosmereScreen
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
