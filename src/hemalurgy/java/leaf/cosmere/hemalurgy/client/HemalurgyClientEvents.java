/*
 * File updated ~ 30 - 1 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.client;

import leaf.cosmere.hemalurgy.client.render.renderer.KolossRenderer;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, value = Dist.CLIENT)
public class HemalurgyClientEvents
{
	@SubscribeEvent
	public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		//I don't think this actually works for this?
		//see the other reference to koloss renderer constructor
		event.registerEntityRenderer(HemalurgyEntityTypes.KOLOSS.get(), KolossRenderer::new);
	}
}
