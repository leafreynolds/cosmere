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
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
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
		if (isMouseOver(pMouseX, pMouseY) && hasManifestation)
		{
			if (pButton == 0)
				Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(manifestation, 1));
			else
				Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(manifestation, -1));

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
			r *= 0.1f;
			g *= 0.1f;
			b *= 0.1f;
		}

		if (isHovered && hasManifestation)
		{
			r *= 1.1f;
			g *= 1.1f;
			b *= 1.1f;
		}

		if (manifestation instanceof FeruchemyManifestation feruchemyManifestation) {
			int mode = feruchemyManifestation.getMode(spiritweb);
			float intensity = Math.min(Math.abs(mode) * 0.2f, 1.0f); // Cap intensity

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

	public float lerp(float start, float end, float pct) {
		return start + pct * (end - start);
	}
}
