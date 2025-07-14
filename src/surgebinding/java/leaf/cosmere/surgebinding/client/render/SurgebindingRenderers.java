
/*
 * File updated ~ 14 - 1 - 2025 ~ Leaf
 * File updated ~ 12 - 7 - 2025 ~ Soar
 */

package leaf.cosmere.surgebinding.client.render;

import leaf.cosmere.client.render.CosmereRenderers;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.surgebinding.client.render.renderer.ArmorRenderer;
import leaf.cosmere.surgebinding.client.render.renderer.ChullRenderer;
import leaf.cosmere.surgebinding.client.render.renderer.CrypticRenderer;
import leaf.cosmere.surgebinding.client.render.renderer.HonorsprenRenderer;
import leaf.cosmere.surgebinding.common.items.ShardplateCurioItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingEntityTypes;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class SurgebindingRenderers
{

	public static void register()
	{


		EntityRenderers.register(SurgebindingEntityTypes.CHULL.get(), ChullRenderer::new);
		EntityRenderers.register(SurgebindingEntityTypes.CRYPTIC.get(), CrypticRenderer::new);
		EntityRenderers.register(SurgebindingEntityTypes.HONORSPREN.get(), HonorsprenRenderer::new);

		for (ItemRegistryObject<ShardplateCurioItem> item : SurgebindingItems.SHARDPLATE_SUITS.values())
		{
			CuriosRendererRegistry.register(item.asItem(), ArmorRenderer::new);
		}
	}


}
