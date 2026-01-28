package leaf.cosmere.allomancy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
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
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class InnerRadialButton extends Button
{
	private static final int TEXT_DISTANCE = 30;
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

	protected InnerRadialButton(int centerX, int centerY, int segmentNr, Metals.MetalType metal, ISpiritweb spiritweb)
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
	}

	@Override
	protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		boolean isHover = isMouseOver(pMouseX, pMouseY);
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
			return angle >= start && angle <= end;
		} else {
			return angle >= start || angle <= end;
		}
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

	// inspired by SteelCodeTeam's Metallic Arts https://github.com/SteelCodeTeam/Metallics-Arts/blob/main/src/main/java/net/rudahee/metallics_arts/modules/logic/client/custom_guis/selectors/AllomanticSelector.java
	private void renderSegment(GuiGraphics pGuiGraphics, boolean isHovered)
	{
		float r, g, b;
		float a = 1f;

		if (!hasManifestation)
		{
			r = g = b = 0.1f;
		}
		else
		{
			r = g = b = 0.5f;
		}

		if (isHovered && hasManifestation)
		{
			r = g = b = 0.6f;
		}

		if (manifestation instanceof AllomancyManifestation allomancyManifestation)
		{
			int mode = allomancyManifestation.getMode(spiritweb);

			if (mode > 0)
				r = r + 0.2f * mode;
			else if (mode < 0)
				b = b + 0.2f * -mode;
		}

		float radsPerSegment = (float) Math.PI * 2 / 8;
		float step = (float) Math.PI / 180;
		float radius = outerRadius;

		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		Matrix4f pose = pGuiGraphics.pose().last().pose();
		Tesselator tess = Tesselator.getInstance();
		BufferBuilder buf = tess.getBuilder();

		buf.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		buf.vertex(pose, centerX, centerY, 0).color(r, g, b, a).endVertex();

		for (float f = 0f; f < radsPerSegment + step/2; f += step)
		{
			float rad = f + segmentNr * radsPerSegment;
			float x = centerX + Mth.cos(rad) * radius;
			float y = centerY + Mth.sin(rad) * radius;

			if (f == 0)
			{
				buf.vertex(pose, x, y, 0).color(r,g,b,a).endVertex();
			}
			buf.vertex(pose, x, y, 0).color(r,g,b,a).endVertex();
		}

		tess.end();

		RenderSystem.disableBlend();
	}

	private void renderIcon(@NotNull GuiGraphics pGuiGraphics)
	{
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
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

		double midAngle = (startAngle + endAngle) / 2;
		double midRadius = (innerRadius + outerRadius) / 1.5;
		int iconSize = width-2;
		int posX = centerX + (int)(Math.cos(midAngle) * midRadius) - iconSize/2;
		int posY = centerY + (int)(Math.sin(midAngle) * midRadius) - iconSize/2;

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

		text = "";

		for (String s : SpiritwebMenu.infoText)
		{
			if (s.toLowerCase().contains("a. " + metalType.getName()))
			{
				text = s.split(":")[1].stripLeading();

				int seconds = Integer.parseInt(text);
				int hours = seconds / 3600;
				int minutes = (seconds % 3600) / 60;
				seconds = seconds % 60;

				if (hours > 0)
					text = String.format("%d:%02d:%02d", hours, minutes, seconds);
				else if (minutes > 0)
					text = String.format("%d:%02d", minutes, seconds);
				else
					text = String.format("%02ds", seconds);

				break;
			}
		}

		pGuiGraphics.drawString(font, text, x+5, y+10+font.lineHeight+5, 0xFFFFFFFF);
	}
}
