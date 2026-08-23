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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Feruchemy.MODID, value = Dist.CLIENT)
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
	public static void registerGuiLayers(RegisterGuiLayersEvent event)
	{
		event.registerBelow(
				VanillaGuiLayers.DEBUG_OVERLAY,
				Feruchemy.rl("hud"),
				(guiGraphics, deltaTracker) -> renderNicrosilHUD(guiGraphics)
		);
	}

	public static void renderNicrosilHUD(final GuiGraphics guiGraphics)
	{
		final Minecraft mc = Minecraft.getInstance();
		SpiritwebCapability.get(mc.player).ifPresent(cap ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) cap;
			//actual menu stuff
			NicrosilMenu.instance.postRender(spiritweb);
		});

	}

}
