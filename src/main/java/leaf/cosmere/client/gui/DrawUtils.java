/*
 * File created ~ 5 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelLastEvent;

import java.awt.*;
import java.util.List;

public class DrawUtils
{

	//https://forums.minecraftforge.net/topic/81895-problems-with-rendering-a-line-1152/?tab=comments#comment-388662
	public static void drawLinesFromPoint(RenderLevelLastEvent event, Vec3 point, List<Vec3> endPoints, Color color)
	{
		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer builder = buffer.getBuffer(RenderType.LINES);

		//move where the line is in the world. otherwise it is drawn around origin 0,0,0 I think?
		Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		PoseStack matrixStack = event.getPoseStack();
		RenderSystem.lineWidth(3);

		matrixStack.pushPose();
		matrixStack.translate(-view.x, -view.y, -view.z);
		Matrix4f matrix = matrixStack.last().pose();

		for (Vec3 endPoint : endPoints)
		{
			builder.vertex(matrix, (float) point.x(), (float) point.y(), (float) point.z())
					.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
					.endVertex();

			builder.vertex(matrix, (float) endPoint.x(), (float) endPoint.y(), (float) endPoint.z())
					.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
					.endVertex();
		}

		matrixStack.popPose();
		RenderSystem.disableDepthTest();
		buffer.endBatch(RenderType.LINES);

	}
}
