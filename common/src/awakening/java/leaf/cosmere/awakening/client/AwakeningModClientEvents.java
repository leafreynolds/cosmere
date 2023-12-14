/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.awakening.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.awakening.client.render.AwakeningRenderers;
import leaf.cosmere.awakening.common.Awakening;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Awakening.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AwakeningModClientEvents
{
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		AwakeningRenderers.register();
		CosmereAPI.logger.info("Cosmere Awakening mod client setup complete!");
	}

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
	}


	//special thank you to the chisels and bits team who have an awakening of how to register other sprites
	@SubscribeEvent
	public static void registerIconTextures(TextureStitchEvent.Pre event)
	{
		final TextureAtlas map = event.getAtlas();
		if (!map.location().equals(InventoryMenu.BLOCK_ATLAS))
		{
			return;
		}

		event.addSprite(Awakening.rl("icon/awakening"));
	}
}
