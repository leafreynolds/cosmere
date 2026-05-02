package leaf.cosmere.feruchemy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
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

public class DiamondButton extends Button
{
	private final ISpiritweb spiritweb;
	private final Manifestation manifestation;
	private final Metals.MetalType metal;
	private final boolean hasManifestation;
	private final float rotation;

	public DiamondButton(int pX, int pY, int distance, float rotation, Metals.MetalType metal, ISpiritweb spiritweb)
	{
		super(pX, pY, distance, distance, CommonComponents.EMPTY, (button) -> { }, DEFAULT_NARRATION);
		this.rotation = rotation;
		this.spiritweb = spiritweb;
		this.metal = metal;
		manifestation = Manifestations.ManifestationTypes.FERUCHEMY.getManifestation(metal.getID());
		hasManifestation = spiritweb.hasManifestation(manifestation);
	}

	@Override
	protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		boolean isHover = isMouseOver(pMouseX, pMouseY);
		renderDiamond(pGuiGraphics, isHover);
		renderIcon(pGuiGraphics);
		if (isHover && hasManifestation)
		{
			renderInfoBlock(pGuiGraphics);
		}
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
				SpiritwebMenu.selectedManifestation = manifestation;
			}
		}

		return retVal;
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
	{
		CosmereAPI.logger.info("Metal clicked: " + metal.getName() + " | Mouse over: " + isMouseOver(pMouseX, pMouseY));
		if (isMouseOver(pMouseX, pMouseY) && hasManifestation)
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
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	private void renderDiamond(GuiGraphics pGuiGraphics, boolean isHovered)
	{
		float r = 61/255.f, g = 70/255.f, b = 76/255.f;
		float a = 1f;

		if (!hasManifestation)
		{
			r *= 0.6f;
			g *= 0.6f;
			b *= 0.6f;
		}

		if (isHovered && hasManifestation)
		{
			r *= 1.1f;
			g *= 1.1f;
			b *= 1.1f;
		}

		if (manifestation instanceof FeruchemyManifestation feruchemyManifestation) {
			int mode = feruchemyManifestation.getMode(spiritweb);
			float intensity = 0;
			if (mode < 0)
				intensity = Math.min(Math.abs(mode) * (1f/16f), 1.0f);
			if (mode > 0)
				intensity = Math.min(Math.abs(mode) * (1f/5f), 1.0f);

			if (mode > 0) {
				// Blend toward pure red
				r = lerp(r, 1.0f, intensity);
				g = lerp(g, 0.0f, intensity);
				b = lerp(b, 0.0f, intensity);
			} else if (mode < 0) {
				// Blend toward pure blue
				r = lerp(r, 0.0f, intensity);
				g = lerp(g, 0.0f, intensity);
				b = lerp(b, 1.0f, intensity);
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

		float size = getWidth();
		float radius = size / 2.0f;
		float centerX = getX() + radius;
		float centerY = getY() + radius;

		int pixelSize = 1;

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
					float screenX = centerX + x;
					float screenY = centerY + y;

					buf.vertex(pose, screenX, screenY, 0).color(r,g,b,a).endVertex();
					buf.vertex(pose, screenX, screenY + pixelSize, 0).color(r,g,b,a).endVertex();
					buf.vertex(pose, screenX + pixelSize, screenY + pixelSize, 0).color(r,g,b,a).endVertex();
					buf.vertex(pose, screenX + pixelSize, screenY, 0).color(r,g,b,a).endVertex();
				}
			}
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
		final ResourceLocation location = new ResourceLocation(manifestation.getRegistryName().getNamespace(), stringBuilder.toString());
		float alpha = hasManifestation ? 1.0f : 0.25f;
		RenderSystem.setShaderTexture(0, location);
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
		pGuiGraphics.blit(location,
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
		pGuiGraphics.blit(location,
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

	private void renderInfoBlock(GuiGraphics pGuiGraphics)
	{
		Font font = Minecraft.getInstance().font;
		int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		int x = 0;
		int y = 0;
		int width = (int) (screenWidth * 0.3);
		int height = screenHeight / 5;
		int color = 0x99333333;

		boolean isLeft = getX() < screenWidth/2;
		boolean isTop = getY() < screenHeight/2;

		if (!isLeft && !isTop)
		{
			// bottom right display
			x = screenWidth - width - 10;
			y = screenHeight - height - 10;
		}
		if (isLeft && !isTop)
		{
			// bottom left display
			x = 10;
			y = screenHeight - height - 10;
		}
		if (isLeft && isTop)
		{
			// top left display
			x = 10;
			y = 10;
		}
		if (!isLeft && isTop)
		{
			// top right display
			x = screenWidth - width - 10;
			y = 10;
		}

		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		pGuiGraphics.fill(x, y, x + width,  y + height, color);

		String text = I18n.get(manifestation.getTranslationKey());
		pGuiGraphics.drawString(font, text, x+5, y+10, 0xFFFFFFFF);

		if (metal != Metals.MetalType.NICROSIL)
		{
			int seconds = manifestation.getInvestitureRemaining(spiritweb);
			int hours = seconds / 3600;
			int minutes = (seconds % 3600) / 60;
			seconds = seconds % 60;

			if (hours > 0)
			{
				text = String.format("%d:%02d:%02d", hours, minutes, seconds);
			}
			else if (minutes > 0)
			{
				text = String.format("%d:%02d", minutes, seconds);
			}
			else if (seconds > 0)
			{
				text = String.format("%02d", seconds);
			}
			else
			{
				text = "Empty";
			}

			pGuiGraphics.drawString(font, text, x + 5, y + 10 + font.lineHeight + 5, 0xFFFFFFFF);
		}
	}

	public float lerp(float start, float end, float pct) {
		return start + pct * (end - start);
	}
}
