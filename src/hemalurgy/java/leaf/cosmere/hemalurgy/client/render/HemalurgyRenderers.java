/*
 * File updated ~ 30 - 1 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client.render;

import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.hemalurgy.client.render.renderer.KolossRenderer;
import leaf.cosmere.hemalurgy.client.render.renderer.SpikeRenderer;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyEntityTypes;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
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

		EntityRenderers.register(HemalurgyEntityTypes.KOLOSS.get(), KolossRenderer::new);
	}

}
