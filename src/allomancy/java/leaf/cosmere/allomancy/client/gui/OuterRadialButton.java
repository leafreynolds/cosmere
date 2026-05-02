package leaf.cosmere.allomancy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import leaf.cosmere.allomancy.common.config.AllomancyConfigs;
import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.packets.ChangeManifestationModeMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class OuterRadialButton extends Button
{
	private final float outerRadius;
	private final float innerRadius;
	private final double startAngle;
	private final double endAngle;
	private final int segmentNr;
	private final int centerX;
	private final int centerY;
	private final boolean hasManifestation;
	private final Manifestation manifestation;
	private final ISpiritweb spiritweb;
	private final Metals.MetalType metalType;

	protected OuterRadialButton(int centerX, int centerY, int segmentNr, Metals.MetalType metal, ISpiritweb spiritweb)
	{
		super(centerX, centerY, 16, 16, CommonComponents.EMPTY, (button) -> {}, DEFAULT_NARRATION);
		this.spiritweb = spiritweb;
		metalType = metal;
		manifestation = Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalType.getID());
		hasManifestation = this.spiritweb.hasManifestation(manifestation);
		double eighthCircle = Math.toRadians(45.d); // a circle is 360 degrees, / by 8 for 45 degrees, converted to radians
		this.segmentNr = segmentNr;     // MetalType doesn't align with the chart in a pattern that can be programmatically written out; we do this manually
		startAngle = segmentNr * eighthCircle;
		endAngle = startAngle + eighthCircle;
		this.centerX = centerX;
		this.centerY = centerY;

		outerRadius = (float) Minecraft.getInstance().getWindow().getGuiScaledHeight() / 3;
		innerRadius = outerRadius * 0.7f;
	}

	@Override
	protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		boolean isHover =isMouseOver(pMouseX, pMouseY);
		renderSegment(pGuiGraphics, isHover);
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
		double distanceX = mouseX - centerX;
		double distanceY = mouseY - centerY;
		double dSqr = distanceX * distanceX + distanceY * distanceY;

		if (dSqr < innerRadius * innerRadius ||
			dSqr > outerRadius * outerRadius)
		{
			return false;
		}

		double angle = Math.atan2(distanceY, distanceX);

		if (angle < 0) {
			angle += 2 * Math.PI;
		}

		double start = normalizeAngle(startAngle);
		double end = normalizeAngle(endAngle);
		angle = normalizeAngle(angle);

		if (start <= end) {
			retVal = angle >= start && angle <= end;
		} else {
			retVal = angle >= start || angle <= end;
		}

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

	@Override
	public void playDownSound(SoundManager pHandler)
	{
		super.playDownSound(pHandler);
	}

	private double normalizeAngle(double angle)
	{
		if (angle != 0)
		{
			angle = angle % (2 * Math.PI);
			if (angle < 0)
			{
				angle += 2 * Math.PI;
			}
		}
		return angle;
	}

	private void renderSegment(@NotNull GuiGraphics pGuiGraphics, boolean isHovered)
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

		if (manifestation instanceof AllomancyManifestation allomancyManifestation) {
			int mode = allomancyManifestation.getMode(spiritweb);
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

		float radsPerSegment = (float) Math.PI * 2 / 8;
		float startAngle = segmentNr * radsPerSegment;

		float outerRadiusSq = outerRadius * outerRadius;
		float innerRadiusSq = innerRadius * innerRadius;

		// Defines the "chunkiness" of the pixelation
		// 1 GUI pixel = 1 screen pixel at 1x scale, or scaled automatically by the game's GUI scale
		int pixelSize = AllomancyConfigs.CLIENT.pixelationAmount.get();

		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		Matrix4f pose = pGuiGraphics.pose().last().pose();

		Tesselator tess = Tesselator.getInstance();
		BufferBuilder buf = tess.getBuilder();

		buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		int rInt = (int) Math.ceil(outerRadius);

		for (int x = -rInt; x <= rInt; x += pixelSize)
		{
			for (int y = -rInt; y <= rInt; y += pixelSize)
			{
				float pixelCenterX = x + (pixelSize / 2f);
				float pixelCenterY = y + (pixelSize / 2f);

				float distSq = pixelCenterX * pixelCenterX + pixelCenterY * pixelCenterY;

				if (distSq <= outerRadiusSq && distSq >= innerRadiusSq)
				{
					float angle = (float) Math.atan2(pixelCenterY, pixelCenterX);
					if (angle < 0) angle += (float) (Math.PI * 2);

					float diff = angle - startAngle;
					if (diff < 0) diff += (float) (Math.PI * 2);

					if (diff < radsPerSegment)
					{
						float px = centerX + x;
						float py = centerY + y;

						buf.vertex(pose, px, py, 0).color(r, g, b, a).endVertex();
						buf.vertex(pose, px, py + pixelSize, 0).color(r, g, b, a).endVertex();
						buf.vertex(pose, px + pixelSize, py + pixelSize, 0).color(r, g, b, a).endVertex();
						buf.vertex(pose, px + pixelSize, py, 0).color(r, g, b, a).endVertex();
					}
				}
			}
		}

		tess.end();
		RenderSystem.disableBlend();
	}

	private void renderIcon(@NotNull GuiGraphics pGuiGraphics)
	{
		Color metalColor = metalType.getColor();
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

		double midAngle = (startAngle + endAngle) / 2;
		double midRadius = (innerRadius + outerRadius) / 2.0;
		int iconSize = width - 2;
		int posX = centerX + (int)(Math.cos(midAngle) * midRadius) - iconSize/2;
		int posY = centerY + (int)(Math.sin(midAngle) * midRadius) - iconSize/2;

		RenderSystem.setShaderColor(0f, 0f, 0f, alpha);
		pGuiGraphics.blit(location,
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
		int width = screenWidth / 4;
		int height = screenHeight / 5;
		int color = 0x99333333;

		if (segmentNr <= 1)
		{
			// bottom right display
			x = screenWidth - width - 10;
			y = screenHeight - height - 10;
		}
		else if (segmentNr <= 3)
		{
			// bottom left display
			x = 10;
			y = screenHeight - height - 10;
		}
		else if (segmentNr <= 5)
		{
			// top left display
			x = 10;
			y = 10;
		}
		else if (segmentNr <= 7)
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

		int seconds = manifestation.getInvestitureRemaining(spiritweb);
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;

		if (hours > 0)
			text = String.format("%d:%02d:%02d", hours, minutes, seconds);
		else if (minutes > 0)
			text = String.format("%d:%02d", minutes, seconds);
		else if (seconds > 0)
			text = String.format("%02d", seconds);
		else
			text = "Empty";

		pGuiGraphics.drawString(font, text, x+5, y+10+font.lineHeight+5, 0xFFFFFFFF);
	}

	public float lerp(float start, float end, float pct)
	{
		return start + pct * (end - start);
	}
}
