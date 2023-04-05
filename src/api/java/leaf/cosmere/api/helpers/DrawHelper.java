/*
 * File updated ~ 5 - 4 - 2023 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import leaf.cosmere.api.CosmereAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Supplier;

public class DrawHelper
{

	//Draw our allomancy lines
	public static void drawLinesFromPoint(PoseStack poseStack, Vec3 originPoint, Color color, List<Vec3> lineEndPositions)
	{
		poseStack.pushPose();

		//move where the line is in the world. otherwise it is drawn around origin 0,0,0 I think?
		Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

		RenderSystem.disableDepthTest();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		poseStack.translate(-view.x, -view.y, -view.z);

		//Tell the render system we're about to draw our lines
		//Use our line settings, special thanks to chisels and bits showing how that works.
		final VertexConsumer bufferIn = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(CosmereAPIRenderTypes.LINE_OVERLAY.get());

		//For all found things, draw the line
		for (Vec3 endPos : lineEndPositions)
		{
			Matrix4f matrix = poseStack.last().pose();
			final Matrix3f normal = poseStack.last().normal();

			bufferIn.vertex(matrix, (float) originPoint.x(), (float) originPoint.y(), (float) originPoint.z())
					.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
					.normal(normal, 0, 1, 0)
					.endVertex();

			bufferIn.vertex(matrix, (float) endPos.x(), (float) endPos.y(), (float) endPos.z())
					.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
					.normal(normal, 0, 1, 0)
					.endVertex();
		}

		Minecraft.getInstance().renderBuffers().bufferSource().endBatch(CosmereAPIRenderTypes.LINE_OVERLAY.get());
		poseStack.popPose();
		RenderSystem.enableDepthTest();
	}

	public static void drawBlocksAtPoint(PoseStack poseStack, Color color, List<BlockPos> blockPosList)
	{
		poseStack.pushPose();

		//move where the line is in the world. otherwise it is drawn around origin 0,0,0 I think?
		Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

		RenderSystem.disableDepthTest();
		RenderSystem.setShaderColor(1, 1, 1, 1);


		poseStack.translate(-view.x, -view.y, -view.z);

		//Tell the render system we're about to draw our lines
		//Use our line settings, special thanks to chisels and bits showing how that works.
		final VertexConsumer bufferIn = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(CosmereAPIRenderTypes.BLOCK_OVERLAY.get());

		Matrix4f matrix = poseStack.last().pose();
		//For all found things, draw the line
		for (BlockPos blockPos : blockPosList)
		{
			renderColoredBlock(poseStack, bufferIn, color, blockPos);
		}

		//we are meant to end batches... but if I don't, then the boxes draw over other boxes.
		Minecraft.getInstance().renderBuffers().bufferSource().endBatch(CosmereAPIRenderTypes.BLOCK_OVERLAY.get());
		poseStack.popPose();
		RenderSystem.enableDepthTest();
	}


	protected static void renderColoredBlock(PoseStack poseStack, VertexConsumer builder, Color color, BlockPos pos)
	{
		renderBoxSolid(
				poseStack,
				builder,
				pos,
				color.getRed() / 255f,
				color.getGreen() / 255f,
				color.getBlue() / 255f,
				0.15f);
	}

	protected static void renderBoxSolid(PoseStack poseStack, VertexConsumer builder, BlockPos pos, float r, float g, float b, float alpha)
	{
		double x = pos.getX() - 0.001;
		double y = pos.getY() - 0.001;
		double z = pos.getZ() - 0.001;
		double xEnd = pos.getX() + 1.0015;
		double yEnd = pos.getY() + 1.0015;
		double zEnd = pos.getZ() + 1.0015;

		renderBoxSolid(poseStack, builder, x, y, z, xEnd, yEnd, zEnd, r, g, b, alpha);
	}

	protected static void renderBoxSolid(PoseStack poseStack, VertexConsumer builder, double x, double y, double z, double xEnd, double yEnd, double zEnd, float red, float green, float blue, float alpha)
	{
		float startX = (float) x;
		float startY = (float) y;
		float startZ = (float) z;
		float endX = (float) xEnd;
		float endY = (float) yEnd;
		float endZ = (float) zEnd;

		Matrix4f matrix = poseStack.last().pose();
		final Matrix3f normal = poseStack.last().normal();

		//down
		builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();

		//up
		builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();

		//east
		builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();

		//west
		builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();

		//south
		builder.vertex(matrix, endX, startY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, endY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, endY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, endX, startY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();

		//north
		builder.vertex(matrix, startX, startY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, startX, startY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, startX, endY, endZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
		builder.vertex(matrix, startX, endY, startZ).color(red, green, blue, alpha).normal(normal, 0, 1, 0).endVertex();
	}

	//Special thanks to Chisels and Bits for showing how this works
	public enum CosmereAPIRenderTypes
	{
		LINE_OVERLAY(() -> Internal.LINE_OVERLAY),
		BLOCK_OVERLAY(() -> Internal.BLOCK_OVERLAY);

		private final Supplier<RenderType> typeSupplier;

		CosmereAPIRenderTypes(final Supplier<RenderType> typeSupplier)
		{
			this.typeSupplier = typeSupplier;
		}

		public RenderType get()
		{
			return typeSupplier.get();
		}

		private static class Internal extends RenderType
		{
			private static final RenderType LINE_OVERLAY = RenderType.create(CosmereAPI.COSMERE_MODID + ":lines",
					DefaultVertexFormat.POSITION_COLOR_NORMAL,
					VertexFormat.Mode.LINES,
					256,
					false,
					false,
					CompositeState.builder()
							.setShaderState(RENDERTYPE_LINES_SHADER)
							.setLineState(new LineStateShard(OptionalDouble.of(2.5d)))
							.setLayeringState(VIEW_OFFSET_Z_LAYERING)
							.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
							.setOutputState(TRANSLUCENT_TARGET)
							.setWriteMaskState(COLOR_WRITE)
							.setCullState(NO_CULL)
							.setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
							.createCompositeState(false));

			private static final RenderType BLOCK_OVERLAY = create(CosmereAPI.COSMERE_MODID + ":block_render",
					DefaultVertexFormat.POSITION_COLOR_NORMAL,
					VertexFormat.Mode.QUADS,
					25565,
					false,
					false,
					RenderType.CompositeState.builder()
							.setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
							.setTextureState(NO_TEXTURE)
							.setLayeringState(VIEW_OFFSET_Z_LAYERING) // view_offset_z_layering
							.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
							.setOutputState(TRANSLUCENT_TARGET)
							.setWriteMaskState(COLOR_WRITE)
							.setCullState(NO_CULL)
							.setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
							.createCompositeState(false));


			private Internal(String name, VertexFormat fmt, VertexFormat.Mode glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable)
			{
				super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
				throw new IllegalStateException("This class must not be instantiated");
			}
		}
	}

}
