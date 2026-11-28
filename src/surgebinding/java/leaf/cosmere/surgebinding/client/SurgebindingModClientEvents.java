/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.client.render.SurgebindingLayerDefinitions;
import leaf.cosmere.surgebinding.client.render.SurgebindingRenderers;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.items.IRadiantShardItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.awt.*;

@EventBusSubscriber(modid = Surgebinding.MODID, value = Dist.CLIENT)
public class SurgebindingModClientEvents
{

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiLayersEvent guiOverlaysEvent)
	{
		guiOverlaysEvent.registerAbove(
				VanillaGuiLayers.EXPERIENCE_BAR,
				Surgebinding.rl("hud"),
				(guiGraphics, deltaTracker) -> HUDHandler.onDrawScreenPost(guiGraphics)
		);
	}

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		SurgebindingLayerDefinitions.register(evt);
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event)
	{
		event.register((stack, tintIndex) ->
		{
			//only tint layer0, the plate body. layer1 is the visor overlay
			if (tintIndex != 0)
			{
				return Color.WHITE.getRGB(); // white = no tint
			}

			if (!(stack.getItem() instanceof IRadiantShardItem shardItem)
					|| !(shardItem.getShardData(stack) instanceof DynamicShardplateData plateData))
			{
				return Roshar.getDeadplate().getRGB();
			}

			//order is null on a plate that was never sworn/rolled
			if (!plateData.isLiving() || !plateData.isColored() || plateData.getOrder() == null)
			{
				return Roshar.getDeadplate().getRGB();
			}

			return plateData.getOrder().getPlateColor().getRGB();
			// fallback if no cap
		}, SurgebindingItems.SHARDPLATE.get());
	}


	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		SurgebindingRenderers.register();
		CosmereAPI.logger.info("Surgebinding client render complete!");
	}
}
