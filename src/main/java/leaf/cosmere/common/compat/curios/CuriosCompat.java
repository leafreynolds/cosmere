/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.common.compat.curios;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

public class CuriosCompat
{
	private static boolean curiosModDetected;


	public static boolean CuriosIsPresent()
	{
		return curiosModDetected;
	}

	public static void init()
	{
		curiosModDetected = ModList.get().isLoaded("curios");

		if (!curiosModDetected)
		{
			return;
		}

		//IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		//modBus.addListener(CuriosCompat::registerSlots);
	}


	private static void registerSlots(InterModEnqueueEvent event)
	{
		if (!curiosModDetected)
		{
			return;
		}

		// Handled in datapacks now, leaving this code commented out and this comment in case this breaks in the future and we come looking
		// Docs: https://docs.illusivesoulworks.com/curios/slots/slot-register
	}
}
