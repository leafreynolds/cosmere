package leaf.cosmere.client.render.curio.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.client.render.curio.CuriosLayerDefinitions;
import leaf.cosmere.client.render.curio.model.BraceletModel;
import leaf.cosmere.client.render.curio.model.SpikeModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class BraceletRenderer implements ICurioRenderer
{
	BraceletModel model;

	public BraceletRenderer()
	{
		final ModelPart modelPart = Minecraft.getInstance().getEntityModels().bakeLayer(CuriosLayerDefinitions.BRACELET);
		model = new BraceletModel(modelPart);
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

		this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
		this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		ICurioRenderer.followBodyRotations(entity, this.model);

		this.model.render(stack, slotContext, matrixStack, renderTypeBuffer, light);

	}
}
