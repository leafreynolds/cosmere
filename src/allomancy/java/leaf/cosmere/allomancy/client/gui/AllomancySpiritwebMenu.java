package leaf.cosmere.allomancy.client.gui;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class AllomancySpiritwebMenu extends Screen
{
	LocalPlayer player;
	public AllomancySpiritwebMenu()
	{
		super(Component.literal("Allomancy"));
		player = Minecraft.getInstance().player;
		init();
	}

	@Override
	protected void init()
	{
		SpiritwebCapability.get(player).ifPresent( (iSpiritweb -> {
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 0, iSpiritweb.getAvailableManifestations().get(0)));
		}));
//		for (int i = 0; i < spiritweb.getAvailableManifestations().size(); i++)
//		{
//			if (i < 8)
//			{
//				addRenderableWidget(new OuterRadialButton(width / 2, height / 2, i, spiritweb.getAvailableManifestations().get(i)));
//			}
//		}
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, "allomancy", pMouseX, pMouseY, 0xFFFFFFFF);
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
	}
}
