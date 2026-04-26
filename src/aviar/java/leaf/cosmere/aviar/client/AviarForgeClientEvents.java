/*
 * File updated ~ 26 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.aviar.client;

import leaf.cosmere.common.Cosmere;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class AviarForgeClientEvents
{
	@SubscribeEvent
	public static void onKey(InputEvent.Key event)
	{

	}
}
