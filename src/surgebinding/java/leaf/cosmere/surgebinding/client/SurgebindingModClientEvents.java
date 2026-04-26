package leaf.cosmere.surgebinding.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.surgebinding.client.render.SurgebindingLayerDefinitions;
import leaf.cosmere.surgebinding.client.render.SurgebindingRenderers;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Surgebinding.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class SurgebindingModClientEvents
{
	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event)
	{
		event.registerAbove(
				VanillaGuiLayers.EXPERIENCE_BAR,
				Surgebinding.rl("hud"),
				(gui, deltaTracker) -> HUDHandler.onDrawScreenPost(gui)
		);
	}

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		SurgebindingLayerDefinitions.register(evt);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		SurgebindingRenderers.register();
		CosmereAPI.logger.info("Surgebinding client setup complete!");
	}
}
