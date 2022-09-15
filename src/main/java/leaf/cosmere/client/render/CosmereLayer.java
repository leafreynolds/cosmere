/*
 * File created ~ 12 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.client.render.curio.CosmereRenderers;
import leaf.cosmere.items.ShardplateItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nonnull;

public class CosmereLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M>
{

	private final RenderLayerParent<T, M> renderLayerParent;

	public CosmereLayer(RenderLayerParent<T, M> renderer)
	{
		super(renderer);
		this.renderLayerParent = renderer;
	}

	@Override
	public void render(@Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource renderTypeBuffer,
	                   int light, @Nonnull T livingEntity, float limbSwing, float limbSwingAmount,
	                   float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		matrixStack.pushPose();

		for (ItemStack armorSlot : livingEntity.getArmorSlots())
		{
			if (!armorSlot.isEmpty() && armorSlot.getItem() instanceof ShardplateItem armorItem)
			{
				SlotContext slotContext = new SlotContext(armorItem.getSlot().getName(), livingEntity, armorItem.getSlot().getIndex(), false, true);
				CosmereRenderers.getRenderer(armorItem).ifPresent(
						renderer -> renderer
								.render(armorSlot, slotContext, matrixStack, renderLayerParent,
										renderTypeBuffer, light, limbSwing, limbSwingAmount, partialTicks,
										ageInTicks, netHeadYaw, headPitch));
			}
		}

		matrixStack.popPose();
	}
}
