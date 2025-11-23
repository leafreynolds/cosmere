package leaf.cosmere.allomancy.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
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
import org.jetbrains.annotations.NotNull;

public class OuterRadialButton extends Button
{
	private static final int OUTER_RADIUS = 100;
	private static final int INNER_RADIUS = 60;
	private final double startAngle;
	private final double endAngle;
	private Manifestation manifestation;

	protected OuterRadialButton(int centerX, int centerY, int segmentNr, Manifestation manifestation)
	{
		super(centerX, centerY, 16, 16, CommonComponents.EMPTY, (button) -> {}, DEFAULT_NARRATION);
		this.manifestation = manifestation;
		double eighthCircle = Math.toRadians(45.d); // a circle is 360 degrees, / by 8 for 45 degrees, converted to radians
		startAngle = segmentNr * eighthCircle;   // todo: decided by power ID
		endAngle = startAngle + eighthCircle;
	}

	@Override
	protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		//renderRingSegment(pGuiGraphics, getX(), getY(), 0xFFFFFFFF);
		//renderSegment(pGuiGraphics, isMouseOver(pMouseX, pMouseY));
		//renderIcon(pGuiGraphics);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		double distanceX = mouseX - this.getX();
		double distanceY = mouseY - this.getY();
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

	private void renderSegment(@NotNull GuiGraphics pGuiGraphics, boolean isHovered)
	{
		final int color = isHovered ? 0xFFFFFFFF : 0xAAAAAAFF;
		int segments = 32;
		double angleRange = endAngle - startAngle;

		for (int i = 0; i < segments; i++) {
			double angle1 = startAngle + angleRange * i / segments;
			double angle2 = startAngle + angleRange * (i + 1) / segments;

			int x1Outer = getX() + (int)(Math.cos(angle1) * OUTER_RADIUS);
			int y1Outer = getY() + (int)(Math.sin(angle1) * OUTER_RADIUS);
			int x2Outer = getX() + (int)(Math.cos(angle2) * OUTER_RADIUS);
			int y2Outer = getY() + (int)(Math.sin(angle2) * OUTER_RADIUS);

			int x1Inner = getX() + (int)(Math.cos(angle1) * INNER_RADIUS);
			int y1Inner = getY() + (int)(Math.sin(angle1) * INNER_RADIUS);
			int x2Inner = getX() + (int)(Math.cos(angle2) * INNER_RADIUS);
			int y2Inner = getY() + (int)(Math.sin(angle2) * INNER_RADIUS);

			// Draw a quad (two triangles) to form the ring segment piece
			// Triangle 1: outer1 -> outer2 -> inner1
			drawTriangle(pGuiGraphics, x1Outer, y1Outer, x2Outer, y2Outer, x1Inner, y1Inner, color);
			// Triangle 2: outer2 -> inner2 -> inner1
			drawTriangle(pGuiGraphics, x2Outer, y2Outer, x2Inner, y2Inner, x1Inner, y1Inner, color);

//			pGuiGraphics.fill(x1Inner, y1Inner, x1Outer, y1Outer, color);
//			pGuiGraphics.fill(x1Outer, y1Outer, x2Outer, y2Outer, color);
		}
	}

	private void renderRingSegment(GuiGraphics guiGraphics, int centerX, int centerY, int color) {
		int segments = 32;
		double angleRange = endAngle - startAngle;

		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		float r = ((color >> 16) & 0xFF) / 255f;
		float g = ((color >> 8) & 0xFF) / 255f;
		float b = (color & 0xFF) / 255f;
		float a = ((color >> 24) & 0xFF) / 255f;

		for (int i = 0; i < segments; i++) {
			double angle1 = startAngle + angleRange * i / segments;
			double angle2 = startAngle + angleRange * (i + 1) / segments;

			float x1Outer = centerX + (float)(Math.cos(angle1) * OUTER_RADIUS);
			float y1Outer = centerY + (float)(Math.sin(angle1) * OUTER_RADIUS);
			float x2Outer = centerX + (float)(Math.cos(angle2) * OUTER_RADIUS);
			float y2Outer = centerY + (float)(Math.sin(angle2) * OUTER_RADIUS);

			float x1Inner = centerX + (float)(Math.cos(angle1) * INNER_RADIUS);
			float y1Inner = centerY + (float)(Math.sin(angle1) * INNER_RADIUS);
			float x2Inner = centerX + (float)(Math.cos(angle2) * INNER_RADIUS);
			float y2Inner = centerY + (float)(Math.sin(angle2) * INNER_RADIUS);

			bufferBuilder.vertex(poseStack.last().pose(), x1Inner, y1Inner, 0).color(r, g, b, a).endVertex();
			bufferBuilder.vertex(poseStack.last().pose(), x1Outer, y1Outer, 0).color(r, g, b, a).endVertex();
			bufferBuilder.vertex(poseStack.last().pose(), x2Outer, y2Outer, 0).color(r, g, b, a).endVertex();
			bufferBuilder.vertex(poseStack.last().pose(), x2Inner, y2Inner, 0).color(r, g, b, a).endVertex();
		}

		BufferUploader.drawWithShader(bufferBuilder.end());

		poseStack.popPose();
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
