/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client;

import leaf.cosmere.hemalurgy.client.render.HemalurgyLayerDefinitions;
import leaf.cosmere.hemalurgy.client.render.HemalurgyRenderers;
import leaf.cosmere.hemalurgy.client.render.model.SpikeModel;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HemalurgyClientSetup
{
	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		evt.registerLayerDefinition(HemalurgyLayerDefinitions.SPIKE, SpikeModel::createLayer);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		HemalurgyRenderers.register();
	}


}
