package leaf.cosmere.client.render.curio;

import leaf.cosmere.client.render.curio.renderer.BraceletRenderer;
import leaf.cosmere.client.render.curio.renderer.SpikeRenderer;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.function.Supplier;

public class CurioRenderers
{
	public static void register()
	{
		final Supplier<ICurioRenderer> spikeRenderer = SpikeRenderer::new;
		for (RegistryObject<Item> itemRegistryObject : ItemsRegistry.METAL_SPIKE.values())
		{
			CuriosRendererRegistry.register(itemRegistryObject.get(), spikeRenderer);
		}

		final Supplier<ICurioRenderer> bracelet = BraceletRenderer::new;
		for (RegistryObject<Item> itemRegistryObject : ItemsRegistry.METAL_BRACELETS.values())
		{
			CuriosRendererRegistry.register(itemRegistryObject.get(), bracelet);
		}
		CuriosRendererRegistry.register(ItemsRegistry.BANDS_OF_MOURNING.get(), bracelet);
	}
}
