package leaf.cosmere.feruchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.api.Metals;
import leaf.cosmere.client.gui.CosmereScreen;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.feruchemy.common.Feruchemy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class FeruchemySpiritwebMenu extends CosmereScreen
{
	private static final float QUARTER_PI_F = (float) (Math.PI/2.f);
	private int distance;
	final LocalPlayer player;
	public FeruchemySpiritwebMenu()
	{
		super(Component.literal("Feruchemy"));
		player = Minecraft.getInstance().player;
		init();
	}

	@Override
	protected void init()
	{
		this.width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		this.height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		distance = this.height / 6;
		final float topLeftRot = 0.f;
		final float topRightRot = QUARTER_PI_F;
		final float bottomRightRot = topRightRot + QUARTER_PI_F;
		final float bottomLeftRot = bottomRightRot + QUARTER_PI_F;

		SpiritwebCapability.get(player).ifPresent( (spiritweb -> {
			int x = this.width/2 - distance;
			int y = this.height/2 - distance*2;

			addRenderableWidget(new DiamondButton(x, y,
												  distance, topLeftRot, Metals.MetalType.IRON, spiritweb));
			addRenderableWidget(new DiamondButton(x + distance, y,
												  distance, topRightRot, Metals.MetalType.STEEL, spiritweb));
			addRenderableWidget(new DiamondButton(x + distance, y + distance,
												  distance, bottomRightRot, Metals.MetalType.TIN, spiritweb));
			addRenderableWidget(new DiamondButton(x, y + distance,
												  distance, bottomLeftRot, Metals.MetalType.PEWTER, spiritweb));

			x = this.width / 2;
			y = this.height / 2 - distance;

			addRenderableWidget(new DiamondButton(x, y,
												  distance, topLeftRot, Metals.MetalType.ZINC, spiritweb));
			addRenderableWidget(new DiamondButton(x + distance, y,
												  distance, topRightRot, Metals.MetalType.COPPER, spiritweb));
			addRenderableWidget(new DiamondButton(x + distance, y + distance,
												  distance, bottomRightRot, Metals.MetalType.BRASS, spiritweb));
			addRenderableWidget(new DiamondButton(x, y + distance,
												  distance, bottomLeftRot, Metals.MetalType.BRONZE, spiritweb));

			x = this.width / 2 - distance;
			y = this.height / 2;

			addRenderableWidget(new DiamondButton(x, y,
												  distance, topLeftRot, Metals.MetalType.GOLD, spiritweb));
			addRenderableWidget(new DiamondButton(x + distance, y,
												  distance, topRightRot, Metals.MetalType.ELECTRUM, spiritweb));
			addRenderableWidget(new DiamondButton(x + distance, y + distance,
												  distance, bottomRightRot, Metals.MetalType.CADMIUM, spiritweb));
			addRenderableWidget(new DiamondButton(x, y + distance,
												  distance, bottomLeftRot, Metals.MetalType.BENDALLOY, spiritweb));

			x = this.width / 2 - distance*2;
			y = this.height / 2 - distance;

			addRenderableWidget(new DiamondButton(x, y,
												  distance, topLeftRot, Metals.MetalType.CHROMIUM, spiritweb));
			addRenderableWidget(new DiamondButton(x + distance, y,
												  distance, topRightRot, Metals.MetalType.DURALUMIN, spiritweb));
			addRenderableWidget(new DiamondButton(x + distance, y + distance,
												  distance, bottomRightRot, Metals.MetalType.NICROSIL, spiritweb));
			addRenderableWidget(new DiamondButton(x, y + distance,
												  distance, bottomLeftRot, Metals.MetalType.ALUMINUM, spiritweb));
		}));
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		renderBorder(pGuiGraphics);
	}

	private void renderBorder(GuiGraphics pGuiGraphics)
	{
		final ResourceLocation location = new ResourceLocation(Feruchemy.MODID, "textures/gui/feru_border.png");

		RenderSystem.setShaderTexture(0, location);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

		final int x = width/2 - distance*2, y = height/2 - distance*2;
		final int screenSize = distance*4;
		final int iconSize = 256;

		pGuiGraphics.blit(location,
				x,
				y,
				screenSize,
				screenSize,
				0,
				0,
				iconSize,
				iconSize,
				iconSize,
				iconSize);

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
}
