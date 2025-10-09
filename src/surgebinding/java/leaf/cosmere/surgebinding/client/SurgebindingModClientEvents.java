/*
 * File updated ~ 14 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.client.render.SurgebindingLayerDefinitions;
import leaf.cosmere.surgebinding.client.render.SurgebindingRenderers;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.DynamicShardplateData;
import leaf.cosmere.surgebinding.common.capabilities.ShardData;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.*;

@Mod.EventBusSubscriber(modid = Surgebinding.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SurgebindingModClientEvents
{

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent guiOverlaysEvent)
	{
		guiOverlaysEvent.registerAbove(
				VanillaGuiOverlay.EXPERIENCE_BAR.id(),
				"hud",
				(forgeGui, gui, partialTick, width, height) -> HUDHandler.onDrawScreenPost(gui)
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
		event.register((stack, tintIndex) -> {
			// We only want to tint layer1 (index 1)
			if (tintIndex != 0) return Color.WHITE.getRGB(); // white = no tint

			return (stack.getCapability(ShardData.SHARD_DATA)
					.map(cap -> {
						DynamicShardplateData cap2 = (DynamicShardplateData)cap;
						boolean living = cap2.isLiving();
						boolean colored = cap2.isColored();

						if (!living || !colored) {
							return Roshar.getDeadplate().getRGB(); // grey when either is false
						}

						return cap2.getOrder().getPlateColor().getRGB();

					})
					.orElse(Roshar.getDeadplate().getRGB()));
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
