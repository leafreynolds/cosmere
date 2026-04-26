/*
 * File updated ~ 14 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render.renderer;

import leaf.cosmere.surgebinding.client.render.model.HonorsprenModel;
import leaf.cosmere.surgebinding.common.entity.spren.Honorspren;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HonorsprenRenderer extends MobRenderer<Honorspren, HonorsprenModel>
{
	private static final ResourceLocation ALLAY_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/allay/allay.png");

	public HonorsprenRenderer(EntityRendererProvider.Context p_234551_)
	{
		super(p_234551_, new HonorsprenModel(p_234551_.bakeLayer(ModelLayers.ALLAY)), 0.4F);
		this.addLayer(new ItemInHandLayer<>(this, p_234551_.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(Honorspren pEntity)
	{
		return ALLAY_TEXTURE;
	}

	protected int getBlockLightLevel(Honorspren pEntity, BlockPos blockPos)
	{
		return 15;
	}
}