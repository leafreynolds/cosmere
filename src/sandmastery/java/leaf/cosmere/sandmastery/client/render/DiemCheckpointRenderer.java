package leaf.cosmere.sandmastery.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.sandmastery.common.blocks.entities.DiemCheckpointBE;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public class DiemCheckpointRenderer implements BlockEntityRenderer<DiemCheckpointBE>
{
	private final BlockRenderDispatcher blockRenderer;

	public DiemCheckpointRenderer(BlockEntityRendererProvider.Context context)
	{
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(DiemCheckpointBE checkpoint, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay)
	{
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null || !player.isHolding(SandmasteryBlocks.DIEM_CHECKPOINT.asItem()))
		{
			return;
		}

		BlockState state = checkpoint.getBlockState();
		blockRenderer.getModelRenderer().renderModel(
				poseStack.last(),
				buffers.getBuffer(RenderType.cutout()),
				state,
				blockRenderer.getBlockModel(state),
				1F, 1F, 1F,
				packedLight,
				packedOverlay,
				ModelData.EMPTY,
				RenderType.cutout());
	}
}
