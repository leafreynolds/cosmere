/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.ideals.order;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;

public class EdgedancerIdealStateManager
{
	public static boolean validateIdeal(SpiritwebCapability spiritweb, int ideal)
	{
		return switch (ideal)
		{
			case 1 -> true;
			case 2 -> IWillRememberThoseWhoHaveBeenForgotten(spiritweb);
			case 3 -> IWillListenToThoseWhoHaveBeenIgnored(spiritweb);
			default -> false;
		};
	}

	private static boolean IWillRememberThoseWhoHaveBeenForgotten(SpiritwebCapability spiritweb)
	{
		return false;
	}

	private static boolean IWillListenToThoseWhoHaveBeenIgnored(SpiritwebCapability spiritweb)
	{
		return false;
	}

}
