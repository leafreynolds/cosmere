/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.ideals.order;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;

public class TruthwatcherIdealStateManager
{
	public static boolean validateIdeal(SpiritwebCapability spiritweb, int ideal)
	{
		return switch (ideal)
		{
			case 1 -> true;
			case 2 -> IWillSeekTruthWhereverItIsHidden(spiritweb);
			case 3 -> IWillRevealTruthToAllWhoSeekIt(spiritweb);
			default -> false;
		};
	}

	private static boolean IWillSeekTruthWhereverItIsHidden(SpiritwebCapability spiritweb)
	{
		return false;
	}

	private static boolean IWillRevealTruthToAllWhoSeekIt(SpiritwebCapability spiritweb)
	{
		return false;
	}

}
