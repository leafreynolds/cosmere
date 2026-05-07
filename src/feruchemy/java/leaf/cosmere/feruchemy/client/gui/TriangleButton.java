package leaf.cosmere.feruchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.gui.GuiUtils;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TriangleButton extends Button
{
	private final ResourceLocation iconLocation;
	private final List<GuiUtils.CachedQuad> cachedQuads = new ArrayList<>();
	private final Consumer<Manifestation> manifestationConsumer;
	private final ISpiritweb spiritweb;
	private final Manifestation manifestation;
	private final Metals.MetalType metal;
	private final boolean hasManifestation;
	private final float rotation;

	public TriangleButton(int pX, int pY, int distance, float rotation, Metals.MetalType metal, ISpiritweb spiritweb, Consumer<Manifestation> maniConsumer)
	{
		super(pX, pY, distance, distance, CommonComponents.EMPTY, (button) -> { }, DEFAULT_NARRATION);
		this.rotation = rotation;
		this.spiritweb = spiritweb;
		this.metal = metal;
		manifestation = Manifestations.ManifestationTypes.FERUCHEMY.getManifestation(metal.getID());
		hasManifestation = spiritweb.hasManifestation(manifestation);

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
		iconLocation = new ResourceLocation(manifestation.getRegistryName().getNamespace(), stringBuilder.toString());

		manifestationConsumer = maniConsumer;

		calculateVertexes(pX, pY, getWidth(), rotation);
	}

	@Override
	protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		boolean isHover = isMouseOver(pMouseX, pMouseY);
		renderTriangle(pGuiGraphics, isHover);
		renderIcon(pGuiGraphics);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		boolean retVal;

		float radius = getWidth() / 2.0f;
		float centerX = getX() + radius;
		float centerY = getY() + radius;

		float dx = (float) mouseX - centerX;
		float dy = (float) mouseY - centerY;

		float cos = (float) Math.cos(-rotation);
		float sin = (float) Math.sin(-rotation);

		float px = dx * cos - dy * sin;
		float py = dx * sin + dy * cos;

		retVal = (px <= radius && py <= radius && (px + py) >= 0);

		if (hasManifestation)
		{
			if (retVal)
			{
				manifestationConsumer.accept(manifestation);
			}
		}

		return retVal;
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
	{
		boolean isMouseOver = isMouseOver(pMouseX, pMouseY);
		if (isMouseOver && hasManifestation)
		{
			if (manifestation.hasMenu())
			{
				manifestation.openMenu();
			}
			else
			{
				if (pButton == 0)
					Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(manifestation, 1));
				else
					Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(manifestation, -1));
			}

			playDownSound(Minecraft.getInstance().getSoundManager());
		}
		return isMouseOver;
	}

	private void renderTriangle(GuiGraphics pGuiGraphics, boolean isHovered)
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

		if (manifestation instanceof FeruchemyManifestation feruchemyManifestation)
		{
			int mode = feruchemyManifestation.getMode(spiritweb);
			float intensity = 0;
			if (mode < 0)
				intensity = Math.min(Math.abs(mode) * (1f/16f), 1.0f);
			if (mode > 0)
				intensity = Math.min(Math.abs(mode) * (1f/5f), 1.0f);

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

		Tesselator tess = Tesselator.getInstance();
		BufferBuilder buf = tess.getBuilder();

		buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		for (GuiUtils.CachedQuad quad : cachedQuads)
		{
			float px = quad.px();
			float py = quad.py();
			float size = quad.size();

			buf.vertex(pose, px, py, 0).color(color.getRGB()).endVertex();
			buf.vertex(pose, px, py + size, 0).color(color.getRGB()).endVertex();
			buf.vertex(pose, px + size, py + size, 0).color(color.getRGB()).endVertex();
			buf.vertex(pose, px + size, py, 0).color(color.getRGB()).endVertex();
		}

		tess.end();
		RenderSystem.disableBlend();
	}

	private void renderIcon(GuiGraphics pGuiGraphics)
	{
		Color metalColor = metal.getColor();
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

		int iconSize = width / 4;

		float radius = width / 2.0f;
		float centerX = getX() + radius;
		float centerY = getY() + radius;

		float localCx = radius / 3.0f;
		float localCy = radius / 3.0f;

		float cos = (float) Math.cos(rotation);
		float sin = (float) Math.sin(rotation);

		int posX = (int) (centerX + localCx * cos - localCy * sin) - iconSize / 2;
		int posY = (int) (centerY + localCx * sin + localCy * cos) - iconSize / 2;

		RenderSystem.setShaderColor(0f, 0f, 0f, alpha);
		pGuiGraphics.blit(iconLocation,
				posX + 1,
				posY + 1,
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

	private void calculateVertexes(float posX, float posY, float size, float rotation)
	{
		cachedQuads.clear();

		float radius = size / 2.0f;
		int pixelSize = 1;
		float centerX = posX + radius;
		float centerY = posY + radius;
		int maxR = (int) Math.ceil(Math.sqrt(radius * radius + radius * radius));

		float cos = (float) Math.cos(-rotation);
		float sin = (float) Math.sin(-rotation);

		for (int x = -maxR; x <= maxR; x += pixelSize)
		{
			for (int y = -maxR; y <= maxR; y += pixelSize)
			{
				float pixelCenterX = x + (pixelSize / 2f);
				float pixelCenterY = y + (pixelSize / 2f);

				float px = pixelCenterX * cos - pixelCenterY * sin;
				float py = pixelCenterX * sin + pixelCenterY * cos;

				if (px <= radius && py <= radius && px + py >= 0)
				{
					cachedQuads.add(new GuiUtils.CachedQuad(centerX + x, centerY + y, pixelSize));
				}
			}
		}
	}
}
