/*
 * File updated ~ 26 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.tools.client;

import leaf.cosmere.tools.common.CosmereTools;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = CosmereTools.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ToolsForgeClientEvents
{
	@SubscribeEvent
	public static void onKey(InputEvent.Key event)
	{

	}
}
