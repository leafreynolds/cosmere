/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.ideals.order;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;

public class DustbringerIdealStateManager
{
	public static boolean validateIdeal(SpiritwebCapability spiritweb, int ideal)
	{
		return switch (ideal)
		{
			case 1 -> true;
			case 2 -> ToControlMyPowerIWillControlMyself(spiritweb);
			case 3 -> ToUnderstandMyPowerIWillUnderstandWhatPowerIs(spiritweb);
			default -> false;
		};
	}

	private static boolean ToControlMyPowerIWillControlMyself(SpiritwebCapability spiritweb)
	{
		return false;
	}

	private static boolean ToUnderstandMyPowerIWillUnderstandWhatPowerIs(SpiritwebCapability spiritweb)
	{
		return false;
	}

}
