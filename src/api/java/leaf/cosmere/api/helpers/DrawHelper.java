/*
 * File updated ~ 12 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.api.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.core.Vec3i;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import leaf.cosmere.api.CosmereAPI;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class DrawHelper
{

	//Draw our allomancy lines
	public static void drawLinesFromPoint(PoseStack poseStack, Vec3 originPoint, float range, Color color, List<Vec3> lineEndPositions, Vec3 highlightVector)
	{
		poseStack.pushPose();

		//move where the line is in the world. otherwise it is drawn around origin 0,0,0 I think?
		Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

		RenderSystem.disableDepthTest();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		poseStack.translate(-view.x, -view.y, -view.z);

		//Tell the render system we're about to draw our lines
		//Use our line settings, special thanks to chisels and bits showing how that works.
		final MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		final VertexConsumer bufferIn = bufferSource.getBuffer(CosmereAPIRenderTypes.LINE_OVERLAY.get());

		//For all found things, draw the line
		for (Vec3 endPos : lineEndPositions)
		{
			Color finalColor = color;

			if (highlightVector != null)
			{
				if (endPos.equals(highlightVector))
				{
					finalColor = Color.decode("#66b2ff");
				}
			}

			int alpha = (int) Math.max(0, Math.floor((1 - (originPoint.distanceTo(endPos) / range)) * finalColor.getAlpha()));  // distance dims the lines until out of range
			Matrix4f matrix = poseStack.last().pose();
			final Matrix3f normal = poseStack.last().normal();

			bufferIn.vertex(matrix, (float) originPoint.x(), (float) originPoint.y(), (float) originPoint.z())
					.color(finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue(), alpha)
					.normal(normal, 0, 1, 0)
					.endVertex();

			bufferIn.vertex(matrix, (float) endPos.x(), (float) endPos.y(), (float) endPos.z())
					.color(finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue(), alpha)
					.normal(normal, 0, 1, 0)
					.endVertex();
		}

		bufferSource.endBatch(CosmereAPIRenderTypes.LINE_OVERLAY.get());
		poseStack.popPose();
		RenderSystem.enableDepthTest();
	}

	public static void drawSquareAtPoint(PoseStack pStack, Color color, List<Vec3> squarePosList, Vec3 destinationVec)
	{
		pStack.pushPose();
		Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

		RenderSystem.disableDepthTest();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		pStack.translate(-view.x, -view.y, -view.z);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);

		// set up texture and buffer
		final ResourceLocation icon = new ResourceLocation("minecraft", "textures/particle/note.png");
		final RenderType RENDER_TYPE = CosmereAPIRenderTypes.SQUARE_TEX_OVERLAY(icon);
		final MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		final VertexConsumer bufferIn = bufferSource.getBuffer(RENDER_TYPE);

		final float size = 0.3F;
		for (Vec3 pos : squarePosList)
		{
			Vec3 directionalVec = pos.subtract(destinationVec).normalize();

			Matrix4f matrix4f = pStack.last().pose();
			Matrix3f matrix3f = pStack.last().normal();

			double pitch = Math.asin(-directionalVec.y);
			double yaw = Math.atan2(directionalVec.x, directionalVec.z);

			Quaternionf rotQuat = Axis.YP.rotationDegrees((float) Math.toDegrees(yaw));
			rotQuat.mul(Axis.XP.rotationDegrees((float) Math.toDegrees(pitch) + 90));

			float[] vertices = {
					-size, 0, -size,
					-size, 0, size,
					size, 0, size,
					size, 0, -size
			};

			float[] textureCoords = {
					1.0F, 0.0F,
					1.0F, 1.0F,
					0.0F, 1.0F,
					0.0F, 0.0F,
			};

			for (int i = 0; i < vertices.length; i += 3)
			{
				float vertexX = vertices[i];
				float vertexY = vertices[i + 1];
				float vertexZ = vertices[i + 2];

				Vector3f rotQuatVec = new Vector3f(vertexX, vertexY, vertexZ);
				rotQuatVec.rotate(rotQuat);

				float finalX = (float) (rotQuatVec.x() + pos.x());
				float finalY = (float) (rotQuatVec.y() + pos.y());
				float finalZ = (float) (rotQuatVec.z() + pos.z());

				int textureUCoord = (int) textureCoords[i / 3 * 2];
				int textureVCoord = (int) textureCoords[i / 3 * 2 + 1];

				squareTexVertex(bufferIn, matrix4f, matrix3f, 1, finalX, finalY, finalZ, textureUCoord, textureVCoord, color);
			}
		}

		bufferSource.endBatch(RENDER_TYPE);
		pStack.popPose();
		RenderSystem.enableDepthTest();
	}

	//copied from DragonFireballRenderer.java
	private static void squareTexVertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int uv2, float pX, float pY, float pZ, int pU, int pV, Color color)
	{
		vertexConsumer.vertex(matrix4f, pX, pY, pZ)
				.uv((float) pU, (float) pV).color(color.getRed(), color.getGreen(), color.getBlue(), 127)
				.overlayCoords(OverlayTexture.NO_OVERLAY).uv2(uv2)
				.normal(matrix3f, 0.0F, 1.0F, 0.0F)
				.endVertex();
	}

	public static void drawBlocksAtPoint(PoseStack poseStack, Color color, List<BlockPos> blockPosList, Vec3 highlightVector, ArrayList<BlockPos> targetedClusterBlockList)
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
			Color finalColor = color;
			if (highlightVector != null)
			{
				if (targetedClusterBlockList.contains(blockPos))
				{
					finalColor = Color.decode("#66b2ff");
				}
				else if (blockPos.getCenter().equals(highlightVector))
				{
					finalColor = Color.decode("#66b2ff");
				}
			}

			renderColoredBlock(poseStack, bufferIn, finalColor, blockPos);
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

		public static RenderType SQUARE_TEX_OVERLAY(ResourceLocation icon)
		{
			return Internal.SQUARE_OVERLAY.apply(icon, true);
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

			private static final BiFunction<ResourceLocation, Boolean, RenderType> SQUARE_OVERLAY = Util.memoize((icon, createComposite) ->
			{
				return create(CosmereAPI.COSMERE_MODID + ":square_render",
						DefaultVertexFormat.POSITION_TEX,
						VertexFormat.Mode.QUADS,
						25565,
						false,
						false,
						RenderType.CompositeState.builder()
								.setShaderState(RenderStateShard.POSITION_TEX_SHADER)
								.setTextureState(new RenderStateShard.TextureStateShard(icon, false, false))
								.setLayeringState(VIEW_OFFSET_Z_LAYERING) // view_offset_z_layering
								.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
								.setOutputState(TRANSLUCENT_TARGET)
								.setWriteMaskState(COLOR_WRITE)
								.setCullState(NO_CULL)
								.setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
								.createCompositeState(createComposite));
			});


			private Internal(String name, VertexFormat fmt, VertexFormat.Mode glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable)
			{
				super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
				throw new IllegalStateException("This class must not be instantiated");
			}
		}
	}

}
