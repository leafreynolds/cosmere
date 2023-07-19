/*
 * File updated ~ 19 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client.render;

import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.hemalurgy.client.render.renderer.KolossLargeRenderer;
import leaf.cosmere.hemalurgy.client.render.renderer.KolossMediumRenderer;
import leaf.cosmere.hemalurgy.client.render.renderer.KolossSmallRenderer;
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

		EntityRenderers.register(HemalurgyEntityTypes.KOLOSS_LARGE.get(), KolossLargeRenderer::new);
		EntityRenderers.register(HemalurgyEntityTypes.KOLOSS_MEDIUM.get(), KolossMediumRenderer::new);
		EntityRenderers.register(HemalurgyEntityTypes.KOLOSS_SMALL.get(), KolossSmallRenderer::new);
	}

}
