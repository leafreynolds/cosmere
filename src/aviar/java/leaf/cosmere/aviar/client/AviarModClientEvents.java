/*
 * File updated ~ 26 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.aviar.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.aviar.client.render.layers.AviarOnShoulderLayer;
import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.common.registries.AviarEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Aviar.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class AviarModClientEvents
{
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		//register the parrot as a normal rendering mob entity
		EntityRenderers.register(AviarEntityTypes.AVIAR_ENTITY.get(), ParrotRenderer::new);
		CosmereAPI.logger.info("Cosmere Aviar mod client setup complete!");
	}

	@SubscribeEvent
	public static void addLayers(EntityRenderersEvent.AddLayers evt)
	{
		//add parrot layers to the player model
		addPlayerLayer(evt, PlayerSkin.Model.WIDE);
		addPlayerLayer(evt, PlayerSkin.Model.SLIM);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void addPlayerLayer(EntityRenderersEvent.AddLayers evt, PlayerSkin.Model skin)
	{
		EntityRenderer<? extends Player> renderer = evt.getSkin(skin);

		if (renderer instanceof LivingEntityRenderer livingRenderer)
		{
			livingRenderer.addLayer(new AviarOnShoulderLayer(livingRenderer, evt.getEntityModels()));
		}
	}
}
