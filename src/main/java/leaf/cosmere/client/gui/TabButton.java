package leaf.cosmere.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.api.Manifestations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TabButton extends Button
{
	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private static final int START_Y = 10;
	public Manifestations.ManifestationTypes manifestation;

	public TabButton(int x, Button.OnPress onPress, Manifestations.ManifestationTypes manifestation)
	{
		super(x, START_Y, WIDTH, HEIGHT, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
		this.manifestation = manifestation;
	}

	@Override
	protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		renderBackground(pGuiGraphics, pMouseX, pMouseY);
		renderIcon(pGuiGraphics);
	}

	private void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY)
	{
		int color = isMouseOver(pMouseX, pMouseY) ? 0x00000077 : 0x00220077;
		//int color = isMouseOver(pMouseX, pMouseY) ? 0xDDDDDDFF : 0x222222FF;

		int minX = this.getX(), minY = this.getY(), maxX = minX + this.width, maxY = minY + this.height;
		pGuiGraphics.fill(minX, minY, maxX, maxY, color);
	}

	private void renderIcon(GuiGraphics pGuiGraphics)
	{
		final ResourceLocation resourceLocation = new ResourceLocation(manifestation.getName(), "textures/icon/" + manifestation.getName() + ".png");
		RenderSystem.setShaderTexture(0, resourceLocation);

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		pGuiGraphics.blit(
				resourceLocation,
				getX(),
				getY(),
				0,
				0,
				width,
				height,
				width,
				height
		);
	}
}
