/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.client.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.sandmastery.client.gui.HUDHandler;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Sandmastery.MODID, value = Dist.CLIENT)
public class SandmasteryClientEvents
{

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event)
	{
		event.registerBelow(
				VanillaGuiLayers.FOOD_LEVEL,
				Sandmastery.rl("hydration_hud"),
				(guiGraphics, deltaTracker) -> HUDHandler.onDrawScreenPost(guiGraphics)
		);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		CosmereAPI.logger.info("Sandmastery client setup complete!");
	}
}
