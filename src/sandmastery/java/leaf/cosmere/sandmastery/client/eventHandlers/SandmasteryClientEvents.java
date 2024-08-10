/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.client.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.sandmastery.client.gui.HUDHandler;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Sandmastery.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SandmasteryClientEvents
{

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent guiOverlaysEvent)
	{
		guiOverlaysEvent.registerBelow(
				VanillaGuiOverlay.FOOD_LEVEL.id(),
				"hud",
				(forgeGui, guiGraphics, partialTick, width, height) -> HUDHandler.onDrawScreenPost(guiGraphics)
		);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		CosmereAPI.logger.info("Sandmastery client setup complete!");
	}
}
