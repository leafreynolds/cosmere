/*
 * File updated ~ 26 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.example.client;

import leaf.cosmere.example.common.Example;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = Example.MODID, bus = Bus.GAME, value = Dist.CLIENT)
public class ExampleForgeClientEvents
{
	@SubscribeEvent
	public static void onKey(InputEvent.Key event)
	{

	}
}
