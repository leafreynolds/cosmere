package leaf.cosmere.sandmastery.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.sandmastery.common.entities.DiemTargetEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public class DiemTargetRenderer extends EntityRenderer<DiemTargetEntity>
{
	private static final BlockState REAL_TARGET = Blocks.TARGET.defaultBlockState();
	private static final BlockState DECOY = Blocks.RED_WOOL.defaultBlockState();

	private final BlockRenderDispatcher blockRenderer;

	public DiemTargetRenderer(EntityRendererProvider.Context context)
	{
		super(context);
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(DiemTargetEntity target, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight)
	{
		poseStack.pushPose();
		float size = target.getSize();
		poseStack.scale(size, size, size);
		poseStack.translate(-0.5, 0, -0.5);
		blockRenderer.renderSingleBlock(target.isDecoy() ? DECOY : REAL_TARGET, poseStack, buffers, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
		poseStack.popPose();
		super.render(target, entityYaw, partialTick, poseStack, buffers, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(DiemTargetEntity target)
	{
		return InventoryMenu.BLOCK_ATLAS;
	}
}
