/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.soulforgery.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.soulforgery.client.render.SoulforgeryRenderers;
import leaf.cosmere.soulforgery.common.Soulforgery;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Soulforgery.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class SoulforgeryModClientEvents
{
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		SoulforgeryRenderers.register();
		CosmereAPI.logger.info("Cosmere Soulforgery mod client setup complete!");
	}

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
	}

}
