/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.surgebinding.client.render.SurgebindingLayerDefinitions;
import leaf.cosmere.surgebinding.client.render.model.ShardbladeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;


public class ShardbladeItemRenderer extends BlockEntityWithoutLevelRenderer
{
	private ShardbladeModel shardbladeModel;
	private final EntityModelSet modelSet;


	public ShardbladeItemRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet)
	{
		super(pBlockEntityRenderDispatcher, pEntityModelSet);
		this.modelSet = pEntityModelSet;
		shardbladeModel = new ShardbladeModel(this.modelSet.bakeLayer(SurgebindingLayerDefinitions.SHARDBLADE));
	}

	@Override
	public void onResourceManagerReload(ResourceManager pResourceManager)
	{
		super.onResourceManagerReload(pResourceManager);
		shardbladeModel = new ShardbladeModel(this.modelSet.bakeLayer(SurgebindingLayerDefinitions.SHARDBLADE));
	}

	public void renderByItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay)
	{
		Item item = itemStack.getItem();
		// todo get which pieces should be active and which should be turned off.

		this.shardbladeModel.setup(itemStack);

		poseStack.pushPose();
		poseStack.scale(1.0F, -1.0F, -1.0F);
		VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(bufferSource, this.shardbladeModel.renderType(ShardbladeModel.TEXTURE), false, itemStack.hasFoil());
		this.shardbladeModel.renderToBuffer(poseStack, vertexconsumer1, pPackedLight, pPackedOverlay, 0xFFFFFFFF);
		poseStack.popPose();

	}
}
