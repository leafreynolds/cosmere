/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.ideals.order;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;

public class SkybreakerIdealStateManager
{
	public static boolean validateIdeal(SpiritwebCapability spiritweb, int ideal)
	{
		return switch (ideal)
		{
			case 1 -> true;
			case 2 -> ISwearToSeekJusticeToLetItGuideMeUntilIFindAMorePerfectIdeal(spiritweb);
			case 3 -> IdealOfDedication(spiritweb);
			case 4 -> IdealOfCrusade(spiritweb);
			case 5 -> IAmTHELAW(spiritweb);
			default -> false;
		};
	}

	private static boolean ISwearToSeekJusticeToLetItGuideMeUntilIFindAMorePerfectIdeal(SpiritwebCapability spiritweb)
	{

		return false;
	}

	private static boolean IdealOfDedication(SpiritwebCapability spiritweb)
	{
		return false;
	}

	private static boolean IdealOfCrusade(SpiritwebCapability spiritweb)
	{
		return false;
	}

	private static boolean IAmTHELAW(SpiritwebCapability spiritweb)
	{
		return false;
	}
}
