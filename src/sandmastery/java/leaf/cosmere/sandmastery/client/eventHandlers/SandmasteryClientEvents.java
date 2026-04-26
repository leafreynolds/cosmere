/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery.client.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.sandmastery.client.gui.HUDHandler;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Sandmastery.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class SandmasteryClientEvents
{

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event)
	{
		event.registerBelow(
				VanillaGuiLayers.FOOD_LEVEL,
				Sandmastery.rl("hud"),
				(guiGraphics, deltaTracker) -> HUDHandler.onDrawScreenPost(guiGraphics)
		);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		CosmereAPI.logger.info("Sandmastery client setup complete!");
	}
}
