/*
 * File updated ~ 26 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.awakening.client;

import leaf.cosmere.awakening.common.Awakening;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = Awakening.MODID, bus = Bus.GAME, value = Dist.CLIENT)
public class AwakeningForgeClientEvents
{
	@SubscribeEvent
	public static void onKey(InputEvent.Key event)
	{

	}
}
