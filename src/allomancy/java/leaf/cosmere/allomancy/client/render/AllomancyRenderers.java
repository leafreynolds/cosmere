/*
 * File updated ~ 5 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.render;

import leaf.cosmere.allomancy.client.render.renderer.MistcloakRenderer;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.client.render.CosmereRenderers;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.function.Supplier;

public class AllomancyRenderers
{

	public static void register()
	{
		final Supplier<ICurioRenderer> mistcloak = MistcloakRenderer::new;
		CosmereRenderers.register(AllomancyItems.MISTCLOAK.get(), mistcloak);
	}


}
