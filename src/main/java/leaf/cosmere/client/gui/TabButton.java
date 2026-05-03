package leaf.cosmere.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.api.Manifestations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

public class TabButton extends Button
{
	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private static final int START_Y = 10;
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
	}

	private void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY)
	{
		float alpha = isMouseOver(pMouseX, pMouseY) ? 1.0f : 0.3f;
		alpha = (selectedTypeSupplier.get() == this.manifestation) ? 1.0f : alpha;
		final ResourceLocation resourceLocation = new ResourceLocation(manifestation.getName(), "textures/gui/hud_background.png");

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
					0,
					width,
					height,
					width,
					height
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
		final ResourceLocation resourceLocation = new ResourceLocation(manifestation.getName(), "textures/icon/" + manifestation.getName() + ".png");
		RenderSystem.setShaderTexture(0, resourceLocation);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		pGuiGraphics.blit(
				resourceLocation,
				getX()+1,
				getY()+1,
				0,
				0,
				width-2,
				height-2,
				width-2,
				height-2
		);

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
}
