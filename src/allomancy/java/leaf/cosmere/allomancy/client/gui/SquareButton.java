package leaf.cosmere.allomancy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.gui.GuiUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;

public class SquareButton extends Button
{
	private final ResourceLocation iconLocation;
	private final Consumer<Manifestation> manifestationConsumer;
	private final ISpiritweb spiritweb;
	private final Metals.MetalType metalType;
	private final Manifestation manifestation;
	private final boolean hasManifestation;

	protected SquareButton(int pX, int pY, int pSize, Metals.MetalType metal, ISpiritweb spiritweb, Consumer<Manifestation> maniConsumer)
	{
		super(pX, pY, pSize, pSize, CommonComponents.EMPTY, (button -> { }), DEFAULT_NARRATION);

		metalType = metal;
		manifestation = Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalType.getID());
		hasManifestation = spiritweb.hasManifestation(manifestation);
		this.spiritweb = spiritweb;
		manifestationConsumer = maniConsumer;

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.setLength(0);
		stringBuilder.append("textures/icon/")
				.append(manifestation.getManifestationType().getName())
				.append("/");

		// no need for a switch case, always allomancy
		if (manifestation instanceof IHasMetalType metalType)
		{
			stringBuilder.append(metalType.getMetalType().getName());
		}

		stringBuilder.append(".png");
		iconLocation = ResourceLocation.fromNamespaceAndPath(manifestation.getRegistryName().getNamespace(), stringBuilder.toString());
	}

	@Override
	public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		boolean isHovered = isMouseOver(pMouseX, pMouseY);

		renderBackground(pGuiGraphics, isHovered);
		renderIcon(pGuiGraphics);
		renderBorder(pGuiGraphics);
		if (isHovered)
			manifestationConsumer.accept(manifestation);
	}

	private void renderBackground(GuiGraphics pGuiGraphics, boolean isHovered)
	{
		Color color = GuiUtils.BACKGROUND_COLOR;

		if (!hasManifestation)
		{
			color = color.darker();
		}

		if (isHovered && hasManifestation)
		{
			color = color.brighter();
		}

		if (manifestation instanceof AllomancyManifestation allomancyManifestation) {
			int mode = allomancyManifestation.getMode(spiritweb);
			float intensity = Math.min(Math.abs(mode) * 0.2f, 1.0f);

			if (mode > 0)
			{
				color = GuiUtils.shiftColor(color, intensity, GuiUtils.POSITIVE_USE_COLOR);
			}
			else if (mode < 0)
			{
				color = GuiUtils.shiftColor(color, intensity, GuiUtils.NEGATIVE_USE_COLOR);
			}
		}

		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Matrix4f pose = pGuiGraphics.pose().last().pose();

		BufferBuilder buf = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		buf.addVertex(pose, getX(), getY(), 0).setColor(color.getRGB());
		buf.addVertex(pose, getX()+getWidth(), getY(), 0).setColor(color.getRGB());
		buf.addVertex(pose, getX()+getWidth(), getY()+getHeight(), 0).setColor(color.getRGB());
		buf.addVertex(pose, getX(), getY()+getHeight(), 0).setColor(color.getRGB());

		BufferUploader.drawWithShader(buf.buildOrThrow());
		RenderSystem.disableBlend();
	}

	private void renderBorder(GuiGraphics pGuiGraphics)
	{
		int color = 0xff9badb7;
		int thickness = 1;
		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Matrix4f pose = pGuiGraphics.pose().last().pose();

		BufferBuilder buf = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		int x = getX();
		int y = getY();
		int w = getWidth();
		int h = getHeight();

		// Top edge
		buf.addVertex(pose, x, y, 0).setColor(color);
		buf.addVertex(pose, x + w, y, 0).setColor(color);
		buf.addVertex(pose, x + w, y + thickness, 0).setColor(color);
		buf.addVertex(pose, x, y + thickness, 0).setColor(color);

		// Bottom edge
		buf.addVertex(pose, x, y + h - thickness, 0).setColor(color);
		buf.addVertex(pose, x + w, y + h - thickness, 0).setColor(color);
		buf.addVertex(pose, x + w, y + h, 0).setColor(color);
		buf.addVertex(pose, x, y + h, 0).setColor(color);

		// Left edge
		buf.addVertex(pose, x, y + thickness, 0).setColor(color);
		buf.addVertex(pose, x + thickness, y + thickness, 0).setColor(color);
		buf.addVertex(pose, x + thickness, y + h - thickness, 0).setColor(color);
		buf.addVertex(pose, x, y + h - thickness, 0).setColor(color);

		// Right edge
		buf.addVertex(pose, x + w - thickness, y + thickness, 0).setColor(color);
		buf.addVertex(pose, x + w, y + thickness, 0).setColor(color);
		buf.addVertex(pose, x + w, y + h - thickness, 0).setColor(color);
		buf.addVertex(pose, x + w - thickness, y + h - thickness, 0).setColor(color);

		BufferUploader.drawWithShader(buf.buildOrThrow());
		RenderSystem.disableBlend();
	}

	private void renderIcon(GuiGraphics pGuiGraphics)
	{
		Color metalColor = metalType.getColor();
		float r = metalColor.getRed()/255.f, g = metalColor.getGreen()/255.f, b = metalColor.getBlue()/255.f;

		if (!hasManifestation)
		{
			r *= 0.1f;
			g *= 0.1f;
			b *= 0.1f;
		}

		float alpha = hasManifestation ? 1.0f : 0.25f;
		RenderSystem.setShaderTexture(0, iconLocation);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		int iconSize = width-10;
		int posX = getX() + 5;
		int posY = getY() + 5;

		RenderSystem.setShaderColor(0f, 0f, 0f, alpha);
		pGuiGraphics.blit(iconLocation,
				posX+1,
				posY+1,
				iconSize,
				iconSize,
				0,
				0,
				width,
				height,
				width,
				height);

		RenderSystem.setShaderColor(r, g, b, alpha);
		pGuiGraphics.blit(iconLocation,
				posX,
				posY,
				iconSize,
				iconSize,
				0,
				0,
				width,
				height,
				width,
				height);

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public float lerp(float start, float end, float pct) {
		return start + pct * (end - start);
	}
}
