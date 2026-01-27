package leaf.cosmere.allomancy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class InnerRadialButton extends Button
{
	private static final int OUTER_RADIUS = 60;
	private static final int INNER_RADIUS = 0;
	private final double startAngle;
	private final double endAngle;
	private final int segmentNr;
	private final int centerX;
	private final int centerY;
	private final Manifestation manifestation;

	protected InnerRadialButton(int centerX, int centerY, int segmentNr, Manifestation manifestation)
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
		renderSegment(pGuiGraphics, isMouseOver(pMouseX, pMouseY));
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
		float r, g, b, a;

		if (isHovered) {
			r = g = b = a = 1.0f;
		} else {
			r = g = 0.f;
			b = 0.67f;
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
}
