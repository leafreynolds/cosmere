package leaf.cosmere.client.render.curio;

import leaf.cosmere.client.render.armor.ArmorRenderer;
import leaf.cosmere.client.render.curio.renderer.BraceletRenderer;
import leaf.cosmere.client.render.curio.renderer.SpikeRenderer;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CosmereRenderers
{
	private static final Map<Item, Supplier<ICurioRenderer>> RENDERER_REGISTRY = new ConcurrentHashMap<>();
	private static final Map<Item, ICurioRenderer> RENDERERS = new HashMap<>();

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


		final Supplier<ICurioRenderer> shardplate = ArmorRenderer::new;
		register(ItemsRegistry.SHARDPLATE_HELMET.get(), shardplate);
		register(ItemsRegistry.SHARDPLATE_CHEST.get(), shardplate);
		register(ItemsRegistry.SHARDPLATE_LEGGINGS.get(), shardplate);
		register(ItemsRegistry.SHARDPLATE_BOOTS.get(), shardplate);
	}


	public static void register(Item item, Supplier<ICurioRenderer> renderer)
	{
		RENDERER_REGISTRY.put(item, renderer);
	}

	public static Optional<ICurioRenderer> getRenderer(Item item)
	{
		return Optional.ofNullable(RENDERERS.get(item));
	}


	public static void load()
	{
		for (Map.Entry<Item, Supplier<ICurioRenderer>> entry : RENDERER_REGISTRY.entrySet())
		{
			RENDERERS.put(entry.getKey(), entry.getValue().get());
		}
	}
}
