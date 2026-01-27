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
		super.init();
		this.width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		this.height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

		SpiritwebCapability.get(player).ifPresent( (iSpiritweb -> {
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 0, iSpiritweb.getAvailableManifestations().get(0)));
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 1, iSpiritweb.getAvailableManifestations().get(1)));
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 2, iSpiritweb.getAvailableManifestations().get(2)));
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 3, iSpiritweb.getAvailableManifestations().get(3)));
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 4, iSpiritweb.getAvailableManifestations().get(4)));
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 5, iSpiritweb.getAvailableManifestations().get(5)));
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 6, iSpiritweb.getAvailableManifestations().get(6)));
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 7, iSpiritweb.getAvailableManifestations().get(7)));

			addRenderableWidget(new InnerRadialButton(width/2, height/2, 0, iSpiritweb.getAvailableManifestations().get(0)));
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 1, iSpiritweb.getAvailableManifestations().get(1)));
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 2, iSpiritweb.getAvailableManifestations().get(2)));
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 3, iSpiritweb.getAvailableManifestations().get(3)));
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 4, iSpiritweb.getAvailableManifestations().get(4)));
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 5, iSpiritweb.getAvailableManifestations().get(5)));
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 6, iSpiritweb.getAvailableManifestations().get(6)));
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 7, iSpiritweb.getAvailableManifestations().get(7)));
		}));
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
	{
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
	}
}
