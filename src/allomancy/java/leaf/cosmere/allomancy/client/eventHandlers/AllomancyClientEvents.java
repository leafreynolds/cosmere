/*
 * File updated ~ 28 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.client.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Allomancy.MODID, value = Dist.CLIENT)
public class AllomancyClientEvents
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onRecipesUpdated(final RecipesUpdatedEvent event)
	{
		AllomancyIronSteel.invalidateWhitelist();
	}
}
