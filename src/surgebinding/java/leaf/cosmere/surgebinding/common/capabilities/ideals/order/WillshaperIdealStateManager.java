/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.ideals.order;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;

public class WillshaperIdealStateManager
{
	public static boolean validateIdeal(SpiritwebCapability spiritweb, int ideal)
	{
		return switch (ideal)
		{
			case 1 -> true;
			case 2 -> IWillSeekFreedomForThoseInBondage(spiritweb);
			case 3 -> IWillFightOppression(spiritweb);
			default -> false;
		};
	}

	private static boolean IWillSeekFreedomForThoseInBondage(SpiritwebCapability spiritweb)
	{
		return false;
	}

	private static boolean IWillFightOppression(SpiritwebCapability spiritweb)
	{
		return false;
	}

}
