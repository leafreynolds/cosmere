/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.surgebinding.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.surgebinding.client.render.SurgebindingLayerDefinitions;
import leaf.cosmere.surgebinding.client.render.SurgebindingRenderers;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.items.ShardplateCurioItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.awt.Color;

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
	public static void registerItemColors(final RegisterColorHandlersEvent.Item event)
	{
		event.register((stack, layer) ->
		{
			if (layer != 0)
			{
				return 0xFFFFFFFF;
			}
			Color color = ((ShardplateCurioItem) stack.getItem()).getColour(stack);
			return 0xFF000000 | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
		}, SurgebindingItems.SHARDPLATE.get());
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		SurgebindingRenderers.register();
		CosmereAPI.logger.info("Surgebinding client setup complete!");
	}
}
