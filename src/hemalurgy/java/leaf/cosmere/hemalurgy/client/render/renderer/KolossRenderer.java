/*
 * File updated ~ 30 - 1 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client.render.renderer;

import leaf.cosmere.hemalurgy.client.render.HemalurgyLayerDefinitions;
import leaf.cosmere.hemalurgy.client.render.model.KolossModel;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.entity.Koloss;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class KolossRenderer extends MobRenderer<Koloss, KolossModel<Koloss>>
{
	private static final ResourceLocation TEXTURE = Hemalurgy.rl("textures/entity/koloss/koloss.png");

	public KolossRenderer(EntityRendererProvider.Context context)
	{
		super(context, new KolossModel<>(context.bakeLayer(HemalurgyLayerDefinitions.KOLOSS)), 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(Koloss pEntity)
	{
		return TEXTURE;
	}
}
