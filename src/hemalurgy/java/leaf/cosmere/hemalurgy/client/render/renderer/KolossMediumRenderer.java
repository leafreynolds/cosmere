/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client.render.renderer;

import leaf.cosmere.hemalurgy.client.render.HemalurgyLayerDefinitions;
import leaf.cosmere.hemalurgy.client.render.model.KolossMediumModel;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.entity.Koloss;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class KolossMediumRenderer extends MobRenderer<Koloss, KolossMediumModel<Koloss>>
{
	private static final ResourceLocation TEXTURE = Hemalurgy.rl("textures/entity/koloss/koloss_medium.png");

	public KolossMediumRenderer(EntityRendererProvider.Context context)
	{
		super(context, new KolossMediumModel<>(context.bakeLayer(HemalurgyLayerDefinitions.KOLOSS_MEDIUM)), 1.0F);

		/*this.addLayer(new KolossItemInHandLayer<>(this, context.getItemInHandRenderer())
		{
			public void render(PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight, Koloss pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
			{
				//only show weapon if aggressive?
				//if (pLivingEntity.isAggressive())
				{
					super.render(poseStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
				}
			}
		});*/

	}

	@Override
	public ResourceLocation getTextureLocation(Koloss pEntity)
	{
		return TEXTURE;
	}
}
