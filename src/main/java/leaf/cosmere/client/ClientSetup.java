/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.client.render.CosmereRenderers;
import leaf.cosmere.common.Cosmere;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
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
	@SubscribeEvent
	public static void registerIconTextures(TextureStitchEvent.Pre event)
	{
		final TextureAtlas map = event.getAtlas();
		if (!map.location().equals(InventoryMenu.BLOCK_ATLAS))
		{
			return;
		}

		event.addSprite(Cosmere.rl("icon/blank"));
		event.addSprite(Cosmere.rl("icon/arrow_up"));
		event.addSprite(Cosmere.rl("icon/arrow_down"));
		event.addSprite(Cosmere.rl("icon/on"));
		event.addSprite(Cosmere.rl("icon/off"));

	}

}
