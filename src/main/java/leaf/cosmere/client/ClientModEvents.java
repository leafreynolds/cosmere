/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.client.render.CosmereRenderers;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEvents
{
	@SubscribeEvent
	public static void addLayers(EntityRenderersEvent.AddLayers evt)
	{
		CosmereRenderers.load();
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		CosmereAPI.logger.info("Cosmere client setup complete!");
	}

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent event)
	{
		event.registerBelow(
				VanillaGuiLayers.DEBUG_OVERLAY,
				Cosmere.rl("spiritweb_hud"),
				(guiGraphics, deltaTracker) ->
				{
					final Minecraft mc = Minecraft.getInstance();
					if (mc.screen instanceof SpiritwebMenu)
					{
						return;
					}
					SpiritwebCapability.get(mc.player).ifPresent(cap ->
							cap.getSpiritwebHud().render(guiGraphics, 0, 0, deltaTracker.getGameTimeDeltaPartialTick(true)));
				}
		);
	}
}
