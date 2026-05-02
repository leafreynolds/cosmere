package leaf.cosmere.feruchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.api.Metals;
import leaf.cosmere.client.gui.CosmereScreen;
import leaf.cosmere.client.gui.SpiritwebMenu;
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
	private static final ResourceLocation BORDER_LOCATION = new ResourceLocation(Feruchemy.MODID, "textures/gui/feru_border.png");
	private static final ResourceLocation SINGLE_BORDER_LOCATION = new ResourceLocation(Feruchemy.MODID, "textures/gui/feru_single_border.png");
	private static final float QUARTER_PI_F = (float) (Math.PI/2.f);
	private int distance;
	final LocalPlayer player;
	public FeruchemySpiritwebMenu()
	{
		super(Component.literal("Feruchemy"), SpiritwebMenu::selectManiCallback);
		player = Minecraft.getInstance().player;
	}

	@Override
	protected void init()
	{
		distance = this.height / 6;
		final float topLeftRot = 0.f;
		final float topRightRot = QUARTER_PI_F;
		final float bottomRightRot = topRightRot + QUARTER_PI_F;
		final float bottomLeftRot = bottomRightRot + QUARTER_PI_F;

		SpiritwebCapability.get(player).ifPresent( (spiritweb -> {
			int x = this.width/2 - distance;
			int y = this.height/2 - distance*2;

			addRenderableWidget(new TriangleButton(x, y,
												  distance, topLeftRot, Metals.MetalType.IRON, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x + distance, y,
												  distance, topRightRot, Metals.MetalType.STEEL, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x + distance, y + distance,
												  distance, bottomRightRot, Metals.MetalType.TIN, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x, y + distance,
												  distance, bottomLeftRot, Metals.MetalType.PEWTER, spiritweb, manifestationConsumer));

			x = this.width / 2;
			y = this.height / 2 - distance;

			addRenderableWidget(new TriangleButton(x, y,
												  distance, topLeftRot, Metals.MetalType.ZINC, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x + distance, y,
												  distance, topRightRot, Metals.MetalType.COPPER, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x + distance, y + distance,
												  distance, bottomRightRot, Metals.MetalType.BRASS, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x, y + distance,
												  distance, bottomLeftRot, Metals.MetalType.BRONZE, spiritweb, manifestationConsumer));

			x = this.width / 2 - distance;
			y = this.height / 2;

			addRenderableWidget(new TriangleButton(x, y,
												  distance, topLeftRot, Metals.MetalType.GOLD, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x + distance, y,
												  distance, topRightRot, Metals.MetalType.ELECTRUM, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x + distance, y + distance,
												  distance, bottomRightRot, Metals.MetalType.CADMIUM, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x, y + distance,
												  distance, bottomLeftRot, Metals.MetalType.BENDALLOY, spiritweb, manifestationConsumer));

			x = this.width / 2 - distance*2;
			y = this.height / 2 - distance;

			addRenderableWidget(new TriangleButton(x, y,
												  distance, topLeftRot, Metals.MetalType.CHROMIUM, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x + distance, y,
												  distance, topRightRot, Metals.MetalType.DURALUMIN, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x + distance, y + distance,
												  distance, bottomRightRot, Metals.MetalType.NICROSIL, spiritweb, manifestationConsumer));
			addRenderableWidget(new TriangleButton(x, y + distance,
												  distance, bottomLeftRot, Metals.MetalType.ALUMINUM, spiritweb, manifestationConsumer));

			y = this.height/2 + distance;

			addRenderableWidget(new TriangleButton(x, y,
					distance, topRightRot, Metals.MetalType.ATIUM, spiritweb, manifestationConsumer));
		}));
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		renderBorder(pGuiGraphics);
		renderAtiumBorder(pGuiGraphics);
	}

	private void renderBorder(GuiGraphics pGuiGraphics)
	{
		RenderSystem.setShaderTexture(0, BORDER_LOCATION);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

		final int x = width/2 - distance*2, y = height/2 - distance*2;
		final int screenSize = distance*4;
		final int iconSize = 256;

		pGuiGraphics.blit(BORDER_LOCATION,
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

	private void renderAtiumBorder(GuiGraphics pGuiGraphics)
	{
		RenderSystem.setShaderTexture(0, SINGLE_BORDER_LOCATION);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

		final int x = this.width / 2 - distance*2-1, y = this.height/2 + distance+1;
		final int screenSize = distance;
		final int iconSize = 64;

		pGuiGraphics.blit(SINGLE_BORDER_LOCATION,
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
