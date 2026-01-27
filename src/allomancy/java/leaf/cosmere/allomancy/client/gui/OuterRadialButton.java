package leaf.cosmere.allomancy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.network.packets.ChangeManifestationModeMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class OuterRadialButton extends Button
{
	private static final int OUTER_RADIUS = 80;
	private static final int INNER_RADIUS = 60;
	private final double startAngle;
	private final double endAngle;
	private final int segmentNr;
	private final int centerX;
	private final int centerY;
	private final Manifestation manifestation;

	protected OuterRadialButton(int centerX, int centerY, int segmentNr, Manifestation manifestation)
	{
		super(centerX, centerY, 16, 16, CommonComponents.EMPTY, (button) -> {}, DEFAULT_NARRATION);
		this.manifestation = manifestation;
		double eighthCircle = Math.toRadians(45.d); // a circle is 360 degrees, / by 8 for 45 degrees, converted to radians
		this.segmentNr = segmentNr;
		startAngle = segmentNr * eighthCircle;   // todo: decided by power ID
		endAngle = startAngle + eighthCircle;
		this.centerX = centerX;
		this.centerY = centerY;
	}

	@Override
	protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		//CosmereAPI.logger.info("Rendering at " + centerX + " | " + centerY);
		renderSegment(pGuiGraphics, isMouseOver(pMouseX, pMouseY));
		//renderIcon(pGuiGraphics);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		double distanceX = mouseX - centerX;
		double distanceY = mouseY - centerY;
		double dSqr = distanceX * distanceX + distanceY * distanceY;

		if (dSqr < INNER_RADIUS * INNER_RADIUS ||
			dSqr > OUTER_RADIUS * OUTER_RADIUS)
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
		if (pButton == 0)
			Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(manifestation, 1));
		else
			Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(manifestation, -1));
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

	// inspired by SteelCodeTeam's Metallic Arts https://github.com/SteelCodeTeam/Metallics-Arts
	private void renderSegment(@NotNull GuiGraphics pGuiGraphics, boolean isHovered)
	{
		float r, g, b, a;

		if (isHovered) {
			r = g = b = a = 1.0f;
		} else {
			r = g = b = 0.67f;
			a = 1.0f;
		}

		float radsPerSegment = (float) Math.PI * 2 / 8;
		float step = (float) Math.PI / 180;
		float radius = OUTER_RADIUS;

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

		switch (manifestation.getManifestationType())
		{
			case ALLOMANCY:
			case FERUCHEMY:
				if (manifestation instanceof IHasMetalType metalType)
				{
					stringBuilder.append(metalType.getMetalType().getName());
				}
				break;
			case SURGEBINDING:
				stringBuilder.append(manifestation.getName());
			case AON_DOR:
			case AWAKENING:
				break;
		}

		stringBuilder.append(".png");
		final ResourceLocation location = new ResourceLocation(manifestation.getRegistryName().getNamespace(), stringBuilder.toString());
		RenderSystem.setShaderTexture(0, location);

		double midAngle = (startAngle + endAngle) / 2;
		double midRadius = (INNER_RADIUS + OUTER_RADIUS) / 2.0;
		int posX = getX() + (int)(Math.cos(midAngle) * midRadius);
		int posY = getY() + (int)(Math.sin(midAngle) * midRadius);

		pGuiGraphics.blit(location,
				posX,
				posY,
				width-2,
				height-2,
				0,
				0,
				width,
				height,
				width,
				height);
	}

	private void drawTriangle(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int x3, int y3, int color) {
		// This is a simple approach - for proper rendering you'd want to use the actual vertex buffer
		guiGraphics.fill(Math.min(x1, Math.min(x2, x3)), Math.min(y1, Math.min(y2, y3)),
				Math.max(x1, Math.max(x2, x3)), Math.max(y1, Math.max(y2, y3)), color);
	}
}
