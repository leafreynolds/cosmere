/*
 * File created ~ 5 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;
import java.util.List;

public class DrawUtils
{

    //https://forums.minecraftforge.net/topic/81895-problems-with-rendering-a-line-1152/?tab=comments#comment-388662
    public static void drawLinesFromPoint(RenderWorldLastEvent event, Vector3d point, List<Vector3d> endPoints, Color color)
    {
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder = buffer.getBuffer(RenderType.LINES);

        //move where the line is in the world. otherwise it is drawn around origin 0,0,0 I think?
        Vector3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        MatrixStack matrixStack = event.getMatrixStack();
        RenderSystem.lineWidth(3);

        matrixStack.push();
        matrixStack.translate(-view.x, -view.y, -view.z);
        Matrix4f matrix = matrixStack.getLast().getMatrix();

        for (Vector3d endPoint : endPoints)
        {
            builder.pos(matrix,(float) point.getX(), (float) point.getY(), (float) point.getZ())
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();

            builder.pos(matrix, (float) endPoint.getX(), (float) endPoint.getY(), (float) endPoint.getZ())
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();
        }

        matrixStack.pop();
        RenderSystem.disableDepthTest();
        buffer.finish(RenderType.LINES);

    }
}
