/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.client;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.client.render.CosmereRenderers;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
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
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent event)
	{
		event.registerBelow(
				VanillaGuiOverlay.DEBUG_TEXT.id(),
				"hud",
				(gui, guiGraphics, partialTick, width, height) -> renderSpiritwebHUD(guiGraphics)
		);
	}

	public static void renderSpiritwebHUD(final GuiGraphics guiGraphics)
	{
		final Minecraft mc = Minecraft.getInstance();
		SpiritwebCapability.get(mc.player).ifPresent(cap ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) cap;

			//normal hud stuff
			if (mc.screen != SpiritwebMenu.instance)
			{
				spiritweb.renderSelectedHUD(guiGraphics);
			}

			//actual menu stuff
			SpiritwebMenu.instance.postRender(spiritweb);
		});

	}
}
