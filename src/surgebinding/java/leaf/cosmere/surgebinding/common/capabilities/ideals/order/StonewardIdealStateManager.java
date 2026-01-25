/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.ideals.order;

import leaf.cosmere.common.cap.entity.SpiritwebCapability;

public class StonewardIdealStateManager
{
	public static boolean validateIdeal(SpiritwebCapability spiritweb, int ideal)
	{
		return switch (ideal)
		{
			case 1 -> true;
			case 2 -> IWillStepForwardWhenOthersFallBack(spiritweb);
			case 3 -> IWillBeTheFoundationOnWhichOthersCanBuild(spiritweb);
			default -> false;
		};
	}

	private static boolean IWillStepForwardWhenOthersFallBack(SpiritwebCapability spiritweb)
	{
		return false;
	}

	private static boolean IWillBeTheFoundationOnWhichOthersCanBuild(SpiritwebCapability spiritweb)
	{
		return false;
	}

}
