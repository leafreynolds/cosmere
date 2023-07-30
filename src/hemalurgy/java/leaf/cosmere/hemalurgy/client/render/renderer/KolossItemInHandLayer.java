/*
 * File updated ~ 13 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client.render.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class KolossItemInHandLayer<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M>
{
	private final ItemInHandRenderer itemInHandRenderer;

	public KolossItemInHandLayer(RenderLayerParent<T, M> p_234846_, ItemInHandRenderer p_234847_)
	{
		super(p_234846_);
		this.itemInHandRenderer = p_234847_;
	}

	public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
	{
		return;

		}


	protected void renderArmWithItem(LivingEntity pLivingEntity, ItemStack pItemStack, ItemTransforms.TransformType pTransformType, HumanoidArm pArm, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight)
	{
		if (!pItemStack.isEmpty())
		{
			pPoseStack.pushPose();
			this.getParentModel().translateToHand(pArm, pPoseStack);
			pPoseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
			pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
			boolean flag = pArm == HumanoidArm.LEFT;
			pPoseStack.translate((double) ((float) (flag ? -1 : 1) / 16.0F), 0.125D, -0.625D);
			this.itemInHandRenderer.renderItem(pLivingEntity, pItemStack, pTransformType, flag, pPoseStack, pBuffer, pPackedLight);
			pPoseStack.popPose();
		}
	}
}