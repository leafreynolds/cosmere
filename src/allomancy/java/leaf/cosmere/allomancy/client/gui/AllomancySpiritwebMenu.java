package leaf.cosmere.allomancy.client.gui;

import leaf.cosmere.api.Metals;
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
			// Steel
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 4, Metals.MetalType.STEEL, iSpiritweb));
			// Iron
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 5, Metals.MetalType.IRON, iSpiritweb));
			// Zinc
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 6, Metals.MetalType.ZINC, iSpiritweb));
			// Brass
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 7, Metals.MetalType.BRASS, iSpiritweb));
			// Bendalloy
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 0, Metals.MetalType.BENDALLOY, iSpiritweb));
			// Cadmium
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 1, Metals.MetalType.CADMIUM, iSpiritweb));
			// Chromium
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 2, Metals.MetalType.CHROMIUM, iSpiritweb));
			// Nicrosil
			addRenderableWidget(new OuterRadialButton(width/2, height/2, 3, Metals.MetalType.NICROSIL, iSpiritweb));

			// Pewter
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 4, Metals.MetalType.PEWTER, iSpiritweb));
			// Tin
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 5, Metals.MetalType.TIN, iSpiritweb));
			// Copper
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 6, Metals.MetalType.COPPER, iSpiritweb));
			// Bronze
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 7, Metals.MetalType.BRONZE, iSpiritweb));
			// Electrum
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 0, Metals.MetalType.ELECTRUM, iSpiritweb));
			// Gold
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 1, Metals.MetalType.GOLD, iSpiritweb));
			// Aluminum
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 2, Metals.MetalType.ALUMINUM, iSpiritweb));
			// Duralumin
			addRenderableWidget(new InnerRadialButton(width/2, height/2, 3, Metals.MetalType.DURALUMIN, iSpiritweb));
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
