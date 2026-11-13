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
	private static final int START_Y = 0;
	private final Supplier<Manifestations.ManifestationTypes> selectedTypeSupplier;
	public Manifestations.ManifestationTypes manifestation;

	public TabButton(int x, Button.OnPress onPress, Manifestations.ManifestationTypes manifestation, Supplier<Manifestations.ManifestationTypes> selectedTypeSupplier)
	{
		super(  x,
				START_Y,
				getScaledWidth(),
				getScaledHeight(),
				CommonComponents.EMPTY,
				onPress,
				DEFAULT_NARRATION);
		this.manifestation = manifestation;

		this.selectedTypeSupplier = selectedTypeSupplier;
	}

	@Override
	protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		boolean isHover = isMouseOver(pMouseX, pMouseY);
		renderBackground(pGuiGraphics, isHover);
		renderIcon(pGuiGraphics, isHover);
		renderText(pGuiGraphics, isHover);
	}

	private void renderBackground(GuiGraphics pGuiGraphics, boolean isHovered)
	{
		int width = getScaledWidth();
		int height = getScaledHeight();

		float alpha = isHovered ? 1.0f : 0.3f;
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
					width-height,
					width,
					height,
					width,
					width
			);
		}
		else
		{
			pGuiGraphics.fill(getX(), getY(), getX()+width, getY()+height, 0x99333333);
		}

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private void renderIcon(GuiGraphics pGuiGraphics, boolean isHovered)
	{
		float alpha = isHovered ? 1.0f : 0.3f;
		alpha = (selectedTypeSupplier.get() == this.manifestation) ? 1.0f : alpha;
		final ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(manifestation.getName(), "textures/icon/" + manifestation.getName() + ".png");
		RenderSystem.setShaderTexture(0, resourceLocation);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		int size = getScaledHeight();
		pGuiGraphics.blit(
				resourceLocation,
				getX(),
				getY(),
				0,
				0,
				size,
				size,
				size,
				size
		);

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private void renderText(GuiGraphics pGuiGraphics, boolean isHovered)
	{
		int iconSize = getScaledHeight();
		int iconPadding = (int) (getScaledHeight() * 0.125f);
		int alpha = isHovered ? 255 : (int)(255.f*0.3f);
		alpha = (selectedTypeSupplier.get() == this.manifestation) ? 255 : alpha;
		Font font = Minecraft.getInstance().font;
		String text = manifestation.getName().toUpperCase();

		int textWidth = font.width(text);
		int textHeight = font.lineHeight;
		float baseScale = getScaledHeight() / textHeight;
		float maxWidth = getScaledWidth() - iconSize - iconPadding;
		float textScale = Math.min(baseScale, maxWidth/textWidth);

		int x = getX() + iconSize + iconPadding;
		int y = (int) (getY() + getScaledHeight()/2 - (textHeight*textScale)/2)+1;

		int rgb = 0xFFFFFF;
		int textColor = (alpha << 24) | rgb;

		GuiUtils.drawScaledString(font, pGuiGraphics, text, x, y, textScale, textColor, true);
	}

	public static int getScaledWidth()
	{
		return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 8;     // based on scale 5.0, scaledWidth/64
	}

	public static int getScaledHeight()
	{
		return Minecraft.getInstance().getWindow().getGuiScaledHeight() / 18;   // based on scale 5.0, scaledHeight/16
	}
}
