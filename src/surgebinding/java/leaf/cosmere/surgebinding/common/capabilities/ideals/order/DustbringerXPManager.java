package leaf.cosmere.surgebinding.common.capabilities.ideals.order;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;

public class DustbringerXPManager
{
	public int dbXP = 0;

	public static boolean validateIdeal(SpiritwebCapability spiritweb, int ideal)
	{
		//Implement XP thresholds
		return switch (ideal)
		{
			case 1 -> true;
			default -> false;
		};
	}

	public static void xpUp()
	{

	}

	public static void xpDown()
	{

	}
}
