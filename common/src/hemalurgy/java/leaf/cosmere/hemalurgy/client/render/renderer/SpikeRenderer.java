/*
 * File updated ~ 7 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client.render.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.hemalurgy.client.render.HemalurgyLayerDefinitions;
import leaf.cosmere.hemalurgy.client.render.model.SpikeModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class SpikeRenderer implements ICurioRenderer
{
	SpikeModel model;

	public SpikeRenderer()
	{
		final ModelPart modelPart = Minecraft.getInstance().getEntityModels().bakeLayer(HemalurgyLayerDefinitions.SPIKE);
		model = new SpikeModel(modelPart);
	}

	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(
			ItemStack stack,
			SlotContext slotContext,
			PoseStack matrixStack,
			RenderLayerParent<T, M> renderLayerParent,
			MultiBufferSource renderTypeBuffer,
			int light, float limbSwing,
			float limbSwingAmount,
			float partialTicks,
			float ageInTicks,
			float netHeadYaw,
			float headPitch)
	{
		LivingEntity entity = slotContext.entity();

		MobEffectInstance effectInstance = entity.getEffect(MobEffects.INVISIBILITY);
		if (effectInstance != null && effectInstance.getDuration() > 0)
		{
			return;
		}

		this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
		this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		ICurioRenderer.followBodyRotations(entity, this.model);
		this.model.render(stack, slotContext, matrixStack, renderTypeBuffer, light);
	}
}
