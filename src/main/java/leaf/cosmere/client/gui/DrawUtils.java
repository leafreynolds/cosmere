/*
 * File updated ~ 5 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.client.gui;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import leaf.cosmere.common.Cosmere;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Supplier;

public class DrawUtils
{

	//Draw our allomancy lines
	public static void drawLinesFromPoint(PoseStack matrixStack, Vec3 originPoint, Multimap<Color, List<Vec3>> linesToDrawByColor)
	{
		matrixStack.pushPose();

		//move where the line is in the world. otherwise it is drawn around origin 0,0,0 I think?
		Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		matrixStack.translate(-view.x, -view.y, -view.z);

		//Tell the render system we're about to draw our lines
		//Use our line settings, special thanks to chisels and bits showing how that works.
		final VertexConsumer bufferIn = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(ModRenderTypes.MEASUREMENT_LINES.get());

		//For all found things, draw the line
		for (Map.Entry<Color, List<Vec3>> entry : linesToDrawByColor.entries())
		{
			final List<Vec3> endPoints = entry.getValue();
			final Color color = entry.getKey();
			for (Vec3 endPoint : endPoints)
			{
				Matrix4f matrix = matrixStack.last().pose();
				final Matrix3f normal = matrixStack.last().normal();

				bufferIn.vertex(matrix, (float) originPoint.x(), (float) originPoint.y(), (float) originPoint.z())
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
						.normal(normal, 0, 1, 0)
						.endVertex();

				bufferIn.vertex(matrix, (float) endPoint.x(), (float) endPoint.y(), (float) endPoint.z())
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
						.normal(normal, 0, 1, 0)
						.endVertex();
			}
		}

		Minecraft.getInstance().renderBuffers().bufferSource().endBatch(ModRenderTypes.MEASUREMENT_LINES.get());
		matrixStack.popPose();
	}


	//Special thanks to Chisels and Bits for showing how this works
	public enum ModRenderTypes
	{
		MEASUREMENT_LINES(() -> Internal.MEASUREMENT_LINES);

		private final Supplier<RenderType> typeSupplier;

		ModRenderTypes(final Supplier<RenderType> typeSupplier)
		{
			this.typeSupplier = typeSupplier;
		}

		public RenderType get()
		{
			return typeSupplier.get();
		}

		private static class Internal extends RenderType
		{
			private static final RenderType MEASUREMENT_LINES = RenderType.create(Cosmere.MODID + ":lines",
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

			private Internal(String name, VertexFormat fmt, VertexFormat.Mode glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable)
			{
				super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
				throw new IllegalStateException("This class must not be instantiated");
			}
		}
	}

}
