/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.client.eventHandlers;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.sandmastery.client.gui.CheckpointBarHUD;
import leaf.cosmere.sandmastery.client.gui.HUDHandler;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.List;

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
		event.registerAbove(
				VanillaGuiLayers.EXPERIENCE_BAR,
				Sandmastery.rl("checkpoint_bar"),
				(guiGraphics, deltaTracker) -> CheckpointBarHUD.render(guiGraphics)
		);
	}

	// the checkpoint bar takes the XP bar's place while it's showing
	@SubscribeEvent
	public static void hideExperienceBar(RenderGuiLayerEvent.Pre event)
	{
		boolean isExperience = event.getName().equals(VanillaGuiLayers.EXPERIENCE_BAR) || event.getName().equals(VanillaGuiLayers.EXPERIENCE_LEVEL);
		if (isExperience && CheckpointBarHUD.isShowing())
		{
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void clearCheckpointBarOnLogout(ClientPlayerNetworkEvent.LoggingOut event)
	{
		CheckpointBarHUD.setCheckpoints(List.of(), -1);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		CosmereAPI.logger.info("Sandmastery client setup complete!");
	}
}
