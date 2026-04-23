/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * NOTE (Phase 0.8 temporary stub): The render-layer code in this file was written against the
 * pre-1.21 vertex builder (`VertexConsumer#vertex(...).color(...).normal(...).endVertex()`),
 * `RenderType.create(...)` positional overload, and `new ResourceLocation(...)`. All of those
 * changed in 1.21.x. The only consumer is `AllomancySpiritwebSubmodule` (out of scope until the
 * allomancy module port pass). Rather than rewrite the full render pipeline here, the public
 * methods are kept as callable no-ops so the `api` source set compiles. Phase 7 (Items / blocks /
 * entities) or a dedicated render pass will restore the real implementations on the 1.21.x
 * `addVertex/setColor/setNormal(PoseStack.Pose, ...)/setUv/setLight/setOverlay` builder, updated
 * RenderType construction, and `ResourceLocation.fromNamespaceAndPath(...)`.
 */

package leaf.cosmere.api.helpers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DrawHelper
{
	public static void drawLinesFromPoint(PoseStack poseStack, Vec3 originPoint, float range, Color color, List<Vec3> lineEndPositions, Vec3 highlightVector)
	{
		// TODO(Phase 7): restore — see file header note.
	}

	public static void drawSquareAtPoint(PoseStack pStack, Color color, List<Vec3> squarePosList, Vec3 destinationVec)
	{
		// TODO(Phase 7): restore — see file header note.
	}

	public static void drawBlocksAtPoint(PoseStack poseStack, Color color, List<BlockPos> blockPosList, float range, Vec3 highlightVector, ArrayList<BlockPos> targetedClusterBlockList)
	{
		// TODO(Phase 7): restore — see file header note.
	}

	public enum CosmereAPIRenderTypes
	{
		LINE_OVERLAY(() -> null),
		BLOCK_OVERLAY(() -> null);

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
			// TODO(Phase 7): restore — see file header note.
			return null;
		}
	}
}
