/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.client.render.CosmereRenderers;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT)
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


	//special thank you to the chisels and bits team who have an example of how to register other sprites
//	@SubscribeEvent
//	public static void registerIconTextures(TextureStitchEvent event)
//	{
//		final TextureAtlas map = event.getAtlas();
//		if (!map.location().equals(InventoryMenu.BLOCK_ATLAS))
//		{
//			return;
//		}
//
//		event.addSprite(Cosmere.rl("icon/blank"));
//		event.addSprite(Cosmere.rl("icon/arrow_up"));
//		event.addSprite(Cosmere.rl("icon/arrow_down"));
//		event.addSprite(Cosmere.rl("icon/on"));
//		event.addSprite(Cosmere.rl("icon/off"));
//	}


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
