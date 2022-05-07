/*
 * File created ~ 31 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.compat.patchouli;

import leaf.cosmere.utils.helpers.LogHelper;
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
		LogHelper.info("Patchouli detected, cosmere can use it's guide item.");
	}

}
