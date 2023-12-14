/*
 * File updated ~ 26 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render.renderer;

import leaf.cosmere.surgebinding.client.render.SurgebindingLayerDefinitions;
import leaf.cosmere.surgebinding.client.render.model.ChullModel;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.entity.Chull;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChullRenderer extends MobRenderer<Chull, ChullModel<Chull>>
{
	private static final ResourceLocation TEXTURE = Surgebinding.rl("textures/entity/chull/chull.png");

	public ChullRenderer(EntityRendererProvider.Context context)
	{
		super(context, new ChullModel<>(context.bakeLayer(SurgebindingLayerDefinitions.CHULL)), 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(Chull pEntity)
	{
		return TEXTURE;
	}
}
