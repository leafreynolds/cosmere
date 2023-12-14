/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.client.render;

import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.feruchemy.client.render.renderer.BraceletRenderer;
import leaf.cosmere.feruchemy.common.items.BraceletMetalmindItem;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.function.Supplier;

public class FeruchemyRenderers
{
	public static void register()
	{
		final Supplier<ICurioRenderer> bracelet = BraceletRenderer::new;
		for (ItemRegistryObject<BraceletMetalmindItem> itemRegistryObject : FeruchemyItems.METAL_BRACELETS.values())
		{
			CuriosRendererRegistry.register(itemRegistryObject.get(), bracelet);
		}
		CuriosRendererRegistry.register(FeruchemyItems.BANDS_OF_MOURNING.get(), bracelet);

	}

}
