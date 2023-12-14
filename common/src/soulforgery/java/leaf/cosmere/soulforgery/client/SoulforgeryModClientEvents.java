/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.soulforgery.client.render.SoulforgeryRenderers;
import leaf.cosmere.soulforgery.common.Soulforgery;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Soulforgery.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoulforgeryModClientEvents
{
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		SoulforgeryRenderers.register();
		CosmereAPI.logger.info("Cosmere Soulforgery mod client setup complete!");
	}

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
	}


	//special thank you to the chisels and bits team who have an soulforgery of how to register other sprites
	@SubscribeEvent
	public static void registerIconTextures(TextureStitchEvent.Pre event)
	{
		final TextureAtlas map = event.getAtlas();
		if (!map.location().equals(InventoryMenu.BLOCK_ATLAS))
		{
			return;
		}

		event.addSprite(Soulforgery.rl("icon/soulforgery"));
	}
}
