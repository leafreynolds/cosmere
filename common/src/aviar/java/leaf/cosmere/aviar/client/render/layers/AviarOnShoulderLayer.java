package leaf.cosmere.aviar.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.aviar.common.registries.AviarEntityTypes;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AviarOnShoulderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>>
{
	private final ParrotModel model;

	public AviarOnShoulderLayer(RenderLayerParent<T, PlayerModel<T>> pRenderer, EntityModelSet entityModelSet)
	{
		super(pRenderer);
		this.model = new ParrotModel(entityModelSet.bakeLayer(ModelLayers.PARROT));
	}

	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
	{
		this.render(pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pNetHeadYaw, pHeadPitch, true);
		this.render(pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pNetHeadYaw, pHeadPitch, false);
	}

	private void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pNetHeadYaw, float pHeadPitch, boolean pLeftShoulder)
	{
		CompoundTag compoundtag =
				pLeftShoulder ? pLivingEntity.getShoulderEntityLeft() : pLivingEntity.getShoulderEntityRight();
		EntityType.byString(compoundtag.getString("id")).filter((entityType) ->
		{
			return entityType == AviarEntityTypes.AVIAR_ENTITY.get();
		}).ifPresent((entityType) ->
		{
			pMatrixStack.pushPose();
			pMatrixStack.translate(
					pLeftShoulder ? (double) 0.4F : (double) -0.4F,
					pLivingEntity.isCrouching() ? (double) -1.3F : -1.5D, 0.0D);
			VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(ParrotRenderer.PARROT_LOCATIONS[compoundtag.getInt("Variant")]));
			this.model.renderOnShoulder(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, pLimbSwing, pLimbSwingAmount, pNetHeadYaw, pHeadPitch, pLivingEntity.tickCount);
			pMatrixStack.popPose();
		});
	}
}