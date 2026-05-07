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
import leaf.cosmere.client.gui.GuiUtils;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.packets.ChangeManifestationModeMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InnerRadialButton extends Button
{
	private final ResourceLocation iconLocation;
	private final List<GuiUtils.CachedQuad> cachedQuads = new ArrayList<>();
	private final Consumer<Manifestation> manifestationConsumer;
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

	protected InnerRadialButton(int centerX, int centerY, int segmentNr, Metals.MetalType metal, ISpiritweb spiritweb, Consumer<Manifestation> maniConsumer)
	{
		super(centerX, centerY, 16, 16, CommonComponents.EMPTY, (button) -> {}, DEFAULT_NARRATION);
		this.spiritweb = spiritweb;
		metalType = metal;
		manifestation = Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalType.getID());
		hasManifestation = spiritweb.hasManifestation(manifestation);
		double eighthCircle = Math.toRadians(45.d); // a circle is 360 degrees, / by 8 for 45 degrees, converted to radians
		this.segmentNr = segmentNr;
		startAngle = segmentNr * eighthCircle;   // todo: decided by power ID
		endAngle = startAngle + eighthCircle;
		this.centerX = centerX;
		this.centerY = centerY;

		outerRadius = (float) Minecraft.getInstance().getWindow().getGuiScaledHeight() / 3f * 0.7f;
		innerRadius = 0;

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

		calculateVertexes(this.centerX, this.centerY, outerRadius, segmentNr);
	}

	@Override
	protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		boolean isHover = isMouseOver(pMouseX, pMouseY);
		renderSegment(pGuiGraphics, isHover);
		renderIcon(pGuiGraphics);
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

		if (start <= end)
		{
			retVal = angle >= start && angle <= end;
		}
		else
		{
			retVal = angle >= start || angle <= end;
		}

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
			if (pButton == 0)
				Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(manifestation, 1));
			else
				Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(manifestation, -1));

			playDownSound(Minecraft.getInstance().getSoundManager());
		}
		return isMouseOver;
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

	private void renderSegment(GuiGraphics pGuiGraphics, boolean isHovered)
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

		float alpha = hasManifestation ? 1.0f : 0.25f;
		RenderSystem.setShaderTexture(0, iconLocation);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		double midAngle = (startAngle + endAngle) / 2;
		double midRadius = (innerRadius + outerRadius) / 1.5;
		int iconSize = width-2;
		int posX = centerX + (int)(Math.cos(midAngle) * midRadius) - iconSize/2;
		int posY = centerY + (int)(Math.sin(midAngle) * midRadius) - iconSize/2;

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

	private void calculateVertexes(float centerX, float centerY, float outerRadius, int segmentNr)
	{
		cachedQuads.clear();

		float radsPerSegment = (float) Math.PI * 2 / 8;
		float startAngle = segmentNr * radsPerSegment;
		float radiusSq = outerRadius * outerRadius;

		int pixelSize = AllomancyConfigs.CLIENT.pixelationAmount.get();
		int rInt = (int) Math.ceil(outerRadius);

		for (int x = -rInt; x <= rInt; x += pixelSize)
		{
			for (int y = -rInt; y <= rInt; y += pixelSize)
			{
				float pixelCenterX = x + (pixelSize / 2f);
				float pixelCenterY = y + (pixelSize / 2f);

				float distSq = pixelCenterX * pixelCenterX + pixelCenterY * pixelCenterY;

				if (distSq <= radiusSq)
				{
					float angle = (float) Math.atan2(pixelCenterY, pixelCenterX);
					if (angle < 0) angle += (float) (Math.PI * 2);

					float diff = angle - startAngle;
					if (diff < 0) diff += (float) (Math.PI * 2);

					if (diff < radsPerSegment)
					{
						cachedQuads.add(new GuiUtils.CachedQuad(centerX + x, centerY + y, pixelSize));
					}
				}
			}
		}
	}
}
