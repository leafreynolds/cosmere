/*
 * File updated ~ 31 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.common.compat.patchouli;

import leaf.cosmere.api.CosmereAPI;
import net.minecraftforge.fml.ModList;

public class PatchouliCompat
{
	private static boolean patchouliModDetected;

	public static boolean PatchouliIsPresent()
	{
		return patchouliModDetected;
	}

	public static void init()
	{
		patchouliModDetected = ModList.get().isLoaded("patchouli");
		CosmereAPI.logger.info("Patchouli detected, cosmere can use it's guide item.");
	}

}
