/*
 * File updated ~ 26 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.aondor.client;

import leaf.cosmere.aondor.common.AonDor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = AonDor.MODID, bus = Bus.GAME, value = Dist.CLIENT)
public class AonDorForgeClientEvents
{
	@SubscribeEvent
	public static void onKey(InputEvent.Key event)
	{

	}
}
