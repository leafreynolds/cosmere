/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.client.render;

import leaf.cosmere.client.render.CosmereRenderers;
import leaf.cosmere.surgebinding.client.render.renderer.*;
import leaf.cosmere.surgebinding.common.registries.*;
import net.minecraft.client.renderer.entity.*;
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
	}


}
