/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.feruchemy.client.gui.NicrosilMenu;
import leaf.cosmere.feruchemy.client.render.FeruchemyLayerDefinitions;
import leaf.cosmere.feruchemy.client.render.FeruchemyRenderers;
import leaf.cosmere.feruchemy.client.render.model.BraceletModel;
import leaf.cosmere.feruchemy.common.Feruchemy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Feruchemy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FeruchemyClientSetup
{

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		evt.registerLayerDefinition(FeruchemyLayerDefinitions.BRACELET, BraceletModel::createLayer);
		//evt.registerLayerDefinition(FeruchemyLayerDefinitions.NECKLACE, NecklaceModel::createLayer);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		FeruchemyRenderers.register();

		CosmereAPI.logger.info("Feruchemy client setup complete!");
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent event)
	{
		event.registerBelow(
				VanillaGuiOverlay.DEBUG_TEXT.id(),
				"hud",
				(gui, guiGraphics, partialTick, width, height) -> renderNicrosilHUD(guiGraphics)
		);
	}

	public static void renderNicrosilHUD(final GuiGraphics guiGraphics)
	{
		final Minecraft mc = Minecraft.getInstance();
		SpiritwebCapability.get(mc.player).ifPresent(cap ->
		{
			//SpiritwebCapability spiritweb = (SpiritwebCapability) cap;

			//actual menu stuff
			NicrosilMenu.instance.postRender();
		});

	}

}
