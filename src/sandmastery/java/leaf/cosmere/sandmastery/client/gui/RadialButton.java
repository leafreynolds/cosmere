package leaf.cosmere.sandmastery.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.client.gui.GuiUtils;
import leaf.cosmere.client.gui.SpiritwebMenu;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RadialButton extends Button
{
	private final ResourceLocation iconLocation;
	private final List<GuiUtils.CachedQuad> cachedQuads = new ArrayList<>();
	private final Consumer<Manifestation> manifestationConsumer;
	private final int radius, centerX, centerY, segmentNr;
	private final float startAngle;
	private final float endAngle;
	private final Manifestation manifestation;
	protected RadialButton(int pX, int pY, int radius, int segmentNr, Manifestation manifestation, Consumer<Manifestation> maniConsumer)
	{
		super(pX, pY, 16, 16, CommonComponents.EMPTY, (button) -> { }, DEFAULT_NARRATION);
		this.manifestation = manifestation;
		this.radius = radius;
		this.segmentNr = segmentNr;
		centerX = pX;
		centerY = pY;

		float fifthCircle = (float) Math.toRadians(360d/5d);
		startAngle = (float) (fifthCircle*segmentNr);
		endAngle = startAngle + fifthCircle;

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.setLength(0);
		stringBuilder.append("textures/icon/")
				.append(manifestation.getManifestationType().getName())
				.append("/.png");

		iconLocation = new ResourceLocation(manifestation.getRegistryName().getNamespace(), stringBuilder.toString());

		manifestationConsumer = maniConsumer;

		calculateVertexes(centerX, centerY, radius, segmentNr);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		boolean retVal;
		double distanceX = mouseX - centerX;
		double distanceY = mouseY - centerY;
		double dSqr = distanceX * distanceX + distanceY * distanceY;

		if (dSqr > radius * radius)
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

		if (retVal)
		{
			manifestationConsumer.accept(manifestation);
		}


		return retVal;
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
	{
		return false;
	}

	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		boolean isHovered = isMouseOver(pMouseX, pMouseY);
		renderRadial(pGuiGraphics, isHovered);
		renderIcon(pGuiGraphics);
		if (isHovered)
			renderInfoBlock(pGuiGraphics);
	}

	private void renderRadial(GuiGraphics pGuiGraphics, boolean isHovered)
	{
		float r = GuiUtils.BACKGROUND_COLOR.getRed()/255.f, g = GuiUtils.BACKGROUND_COLOR.getGreen()/255.f, b = GuiUtils.BACKGROUND_COLOR.getBlue()/255.f;
		float a = 1f;

		if (isHovered)
		{
			r *= 1.1f;
			g *= 1.1f;
			b *= 1.1f;
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

			buf.vertex(pose, px, py, 0).color(r, g, b, a).endVertex();
			buf.vertex(pose, px, py + size, 0).color(r, g, b, a).endVertex();
			buf.vertex(pose, px + size, py + size, 0).color(r, g, b, a).endVertex();
			buf.vertex(pose, px + size, py, 0).color(r, g, b, a).endVertex();
		}

		tess.end();
		RenderSystem.disableBlend();
	}

	private void renderIcon(GuiGraphics pGuiGraphics)
	{
		float alpha = 1.0f;
		RenderSystem.setShaderTexture(0, iconLocation);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		double midAngle = (startAngle + endAngle) / 2;
		double midRadius = (radius) / 1.5;
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

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
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

	private void renderInfoBlock(GuiGraphics pGuiGraphics)
	{
		Font font = Minecraft.getInstance().font;
		float radsPerSegment = (float) (Math.PI * 2 / 5);
		float midAngle = (segmentNr * radsPerSegment) + (radsPerSegment / 2f);

		int padding = 15;
		float textOffsetDist = radius + padding;

		int textCenterX = (int) (centerX + Math.cos(midAngle) * textOffsetDist);
		int textCenterY = (int) (centerY + Math.sin(midAngle) * textOffsetDist);

		int drawY = textCenterY - (font.lineHeight / 2);

		String text = I18n.get(manifestation.getTranslationKey()).replace("Sand Mastery ", "");

		pGuiGraphics.drawCenteredString(font, text, textCenterX, drawY, 0xFFFFFF);
	}

	private void calculateVertexes(float centerX, float centerY, float radius, int segmentNr)
	{
		cachedQuads.clear();

		float radsPerSegment = (float) Math.PI * 2 / 5;
		float startAngle = segmentNr * radsPerSegment;
		float radiusSq = radius * radius;

		int pixelSize = 1;
		int rInt = (int) Math.ceil(radius);

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
}
