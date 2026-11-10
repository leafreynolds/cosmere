package leaf.cosmere.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.api.Manifestations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

public class TabButton extends Button
{
	private static final int ICON_SIZE = 16;
	private static final int ICON_PADDING = 2;
	private static final int WIDTH = 64;
	private static final int HEIGHT = 16;
	private static final int START_Y = 0;
	private final Supplier<Manifestations.ManifestationTypes> selectedTypeSupplier;
	public Manifestations.ManifestationTypes manifestation;

	public TabButton(int x, Button.OnPress onPress, Manifestations.ManifestationTypes manifestation, Supplier<Manifestations.ManifestationTypes> selectedTypeSupplier)
	{
		super(x, START_Y, WIDTH, HEIGHT, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
		this.manifestation = manifestation;

		this.selectedTypeSupplier = selectedTypeSupplier;
	}

	@Override
	protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		renderBackground(pGuiGraphics, pMouseX, pMouseY);
		renderIcon(pGuiGraphics, pMouseX, pMouseY);
		renderText(pGuiGraphics);
	}

	private void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY)
	{
		float alpha = isMouseOver(pMouseX, pMouseY) ? 1.0f : 0.3f;
		alpha = (selectedTypeSupplier.get() == this.manifestation) ? 1.0f : alpha;
		final ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(manifestation.getName(), "textures/gui/hud_background.png");

		if (Minecraft.getInstance().getResourceManager().getResource(resourceLocation).isPresent())
		{
			RenderSystem.setShaderTexture(0, resourceLocation);
			float[] shaderColor = RenderSystem.getShaderColor();
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			pGuiGraphics.blit(
					resourceLocation,
					getX(),
					getY(),
					0,
					64-16,
					width,
					height,
					64,
					64
			);
		}
		else
		{
			pGuiGraphics.fill(getX(), getY(), getX()+width, getY()+height, 0x99333333);
		}

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private void renderIcon(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY)
	{
		float alpha = isMouseOver(pMouseX, pMouseY) ? 1.0f : 0.3f;
		alpha = (selectedTypeSupplier.get() == this.manifestation) ? 1.0f : alpha;
		final ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(manifestation.getName(), "textures/icon/" + manifestation.getName() + ".png");
		RenderSystem.setShaderTexture(0, resourceLocation);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		pGuiGraphics.blit(
				resourceLocation,
				getX(),
				getY(),
				0,
				0,
				ICON_SIZE,
				ICON_SIZE,
				ICON_SIZE,
				ICON_SIZE
		);

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private void renderText(GuiGraphics pGuiGraphics)
	{
		Font font = Minecraft.getInstance().font;
		String text = manifestation.getName().toUpperCase();

		int textWidth = font.width(text);
		int textHeight = font.lineHeight;
		float textScale = (float)(getWidth() - ICON_SIZE - ICON_PADDING)/textWidth;
		if (textScale > 1.f)
		{
			textScale = 1.f;
		}

		int x = getX() + ICON_SIZE + ICON_PADDING;
		int y = (int) (getY() + getHeight()/2 - (textHeight*textScale)/2)+1;

		GuiUtils.drawScaledString(font, pGuiGraphics, text, x, y, textScale, 0xFFFFFFFF, true);
	}
}
