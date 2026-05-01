package leaf.cosmere.surgebinding.client.gui;

import leaf.cosmere.client.gui.CosmereScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class SurgebindingSpiritwebMenu extends CosmereScreen
{
	public SurgebindingSpiritwebMenu()
	{
		super(Component.literal("Surgebinding"));
	}

	@Override
	protected void init()
	{
		super.init();
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		Font font = Minecraft.getInstance().font;
		pGuiGraphics.drawString(font, "Surgebinding", width/2, height/2, 0xFFFFFFFF);
	}
}
