/*
 * File updated ~ 20 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.client;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.aviar.client.render.AviarRenderers;
import leaf.cosmere.aviar.client.render.layers.AviarOnShoulderLayer;
import leaf.cosmere.aviar.common.Aviar;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Aviar.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AviarModClientEvents
{
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		AviarRenderers.register();
		CosmereAPI.logger.info("Cosmere Aviar mod client setup complete!");
	}

	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		//evt.registerLayerDefinition(AviarLayerDefinitions.AVIAR, ParrotModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void addLayers(EntityRenderersEvent.AddLayers evt)
	{
		addPlayerLayer(evt, "default");
		addPlayerLayer(evt, "slim");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void addPlayerLayer(EntityRenderersEvent.AddLayers evt, String skin)
	{
		EntityRenderer<? extends Player> renderer = evt.getSkin(skin);

		if (renderer instanceof LivingEntityRenderer livingRenderer)
		{
			livingRenderer.addLayer(new AviarOnShoulderLayer(livingRenderer, evt.getEntityModels()));
		}
	}
}
