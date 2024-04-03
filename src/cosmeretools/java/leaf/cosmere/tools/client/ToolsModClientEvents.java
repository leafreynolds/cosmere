/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.tools.client.render.ToolsRenderers;
import leaf.cosmere.tools.common.CosmereTools;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CosmereTools.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ToolsModClientEvents
{
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		ToolsRenderers.register();
		CosmereAPI.logger.info("Cosmere Tools mod client setup complete!");
	}

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
	}


	//special thank you to the chisels and bits team who have an tools of how to register other sprites
	@SubscribeEvent
	public static void registerIconTextures(TextureStitchEvent.Pre event)
	{
		final TextureAtlas map = event.getAtlas();
		if (!map.location().equals(InventoryMenu.BLOCK_ATLAS))
		{
			return;
		}

		event.addSprite(CosmereTools.rl("icon/tools"));
	}
}
