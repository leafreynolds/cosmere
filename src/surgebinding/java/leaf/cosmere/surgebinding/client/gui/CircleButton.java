package leaf.cosmere.surgebinding.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
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

public class CircleButton extends Button
{
	private final ResourceLocation iconLocation;
	private final List<GuiUtils.CachedQuad> cachedQuads = new ArrayList<>();
	private final Consumer<Manifestation> manifestationConsumer;
	final ISpiritweb spiritweb;
	final Manifestation manifestation;
	final int centerX, centerY;
	final int radius;

	public CircleButton(int pX, int pY, int radius, ISpiritweb spiritweb, Manifestation manifestation, Consumer<Manifestation> maniConsumer)
	{
		super(pX, pY, radius, radius, CommonComponents.EMPTY, (button) -> { }, DEFAULT_NARRATION);

		this.spiritweb = spiritweb;
		this.centerX = pX;
		this.centerY = pY;
		this.radius = radius;
		this.manifestation = manifestation;

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.setLength(0);
		stringBuilder.append("textures/icon/")
				.append(manifestation.getManifestationType().getName())
				.append("/");

		stringBuilder.append(manifestation.getName());
		stringBuilder.append(".png");

		iconLocation = new ResourceLocation(manifestation.getRegistryName().getNamespace(), stringBuilder.toString());

		manifestationConsumer = maniConsumer;

		calculateVertexes(centerX, centerY, radius);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		double distanceX = mouseX - centerX;
		double distanceY = mouseY - centerY;

		if ((distanceX * distanceX + distanceY * distanceY) <= (radius * radius)) {
			manifestationConsumer.accept(manifestation);
			return true;
		}

		return false;
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
		renderCircle(pGuiGraphics, isHovered);
		renderIcon(pGuiGraphics);
		if (isHovered)
			renderText(pGuiGraphics);
	}

	private void renderCircle(GuiGraphics pGuiGraphics, boolean isHovered)
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

	public void renderIcon(GuiGraphics pGuiGraphics)
	{
		float alpha = 1.0f;
		RenderSystem.setShaderTexture(0, iconLocation);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		int iconSize = width-2;
		int posX = centerX - iconSize/2;
		int posY = centerY - iconSize/2;

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

	private void renderText(GuiGraphics pGuiGraphics)
	{
		Font font = Minecraft.getInstance().font;
		String text = I18n.get(manifestation.getTranslationKey());
		int x;
		int y = getY();
		int windowWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();

		if (getX() < windowWidth/2)
		{
			x = getX() - radius - 5 - font.width(text);
		}
		else
		{
			x = getX() + radius + 5;
		}

		pGuiGraphics.drawString(font, text, x, y-font.lineHeight/2, 0xFFFFFFFF);
	}

	private void calculateVertexes(float centerX, float centerY, int radius)
	{
		cachedQuads.clear();

		float radiusSq = radius * radius;
		int pixelSize = 1;

		for (int x = -radius; x <= radius; x += pixelSize)
		{
			for (int y = -radius; y <= radius; y += pixelSize)
			{
				float pixelCenterX = x + (pixelSize / 2f);
				float pixelCenterY = y + (pixelSize / 2f);

				if ((pixelCenterX * pixelCenterX + pixelCenterY * pixelCenterY) <= radiusSq)
				{
					cachedQuads.add(new GuiUtils.CachedQuad(centerX + x, centerY + y, pixelSize));
				}
			}
		}
	}
}
