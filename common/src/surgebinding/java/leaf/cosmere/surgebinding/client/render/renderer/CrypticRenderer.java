/*
 * File updated ~ 26 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.surgebinding.client.render.SurgebindingLayerDefinitions;
import leaf.cosmere.surgebinding.client.render.model.CrypticModel;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.entity.Cryptic;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrypticRenderer extends MobRenderer<Cryptic, CrypticModel<Cryptic>>
{
	private static final ResourceLocation TEXTURE = Surgebinding.rl("textures/entity/cryptic/cryptic.png");

	public CrypticRenderer(EntityRendererProvider.Context context)
	{
		super(context, new CrypticModel<>(context.bakeLayer(SurgebindingLayerDefinitions.CRYPTIC)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(Cryptic pEntity) {
		return TEXTURE;
	}

	@Override
	public void render(Cryptic pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
		super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
	}
}
