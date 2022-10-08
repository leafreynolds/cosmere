/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client.render;

import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.hemalurgy.client.render.renderer.SpikeRenderer;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.function.Supplier;

public class HemalurgyRenderers
{
	public static void register()
	{
		final Supplier<ICurioRenderer> spikeRenderer = SpikeRenderer::new;
		for (ItemRegistryObject<HemalurgicSpikeItem> itemRegistryObject : HemalurgyItems.METAL_SPIKE.values())
		{
			CuriosRendererRegistry.register(itemRegistryObject.get(), spikeRenderer);
		}

	}

}
