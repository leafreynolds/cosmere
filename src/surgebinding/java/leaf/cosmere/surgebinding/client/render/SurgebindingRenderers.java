
/*
 * File updated ~ 14 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render;

import leaf.cosmere.client.render.CosmereRenderers;
import leaf.cosmere.surgebinding.client.render.renderer.ArmorRenderer;
import leaf.cosmere.surgebinding.client.render.renderer.ChullRenderer;
import leaf.cosmere.surgebinding.client.render.renderer.CrypticRenderer;
import leaf.cosmere.surgebinding.client.render.renderer.HonorsprenRenderer;
import leaf.cosmere.surgebinding.common.registries.SurgebindingEntityTypes;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.function.Supplier;

public class SurgebindingRenderers
{

	public static void register()
	{
		final Supplier<ICurioRenderer> shardplate = ArmorRenderer::new;
		CosmereRenderers.register(SurgebindingItems.SHARDPLATE_HELMET.get(), shardplate);
		CosmereRenderers.register(SurgebindingItems.SHARDPLATE_CHEST.get(), shardplate);
		CosmereRenderers.register(SurgebindingItems.SHARDPLATE_LEGGINGS.get(), shardplate);
		CosmereRenderers.register(SurgebindingItems.SHARDPLATE_BOOTS.get(), shardplate);

		EntityRenderers.register(SurgebindingEntityTypes.CHULL.get(), ChullRenderer::new);
		EntityRenderers.register(SurgebindingEntityTypes.CRYPTIC.get(), CrypticRenderer::new);
		EntityRenderers.register(SurgebindingEntityTypes.HONORSPREN.get(), HonorsprenRenderer::new);
	}


}
