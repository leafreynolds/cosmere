/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;

public class AllomancyAluminum extends AllomancyManifestation
{
	public AllomancyAluminum(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		//passive active ability, if any
		if (isActiveTick(data))
		{
			int drainedCount = 0;

			//drain all metals
			for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
			{
				if (metalType == Metals.MetalType.ALUMINUM)
				{
					continue;
				}

				if (drainMetal(data, metalType))
				{
					drainedCount++;
				}
			}

			if (drainedCount <= 0)
			{
				drainMetal(data, Metals.MetalType.ALUMINUM);
			}

			data.syncToClients(null);
		}
	}

	private boolean drainMetal(ISpiritweb data, Metals.MetalType metalType)
	{
		//todo move this to the drain investiture effect?
		AllomancySpiritwebSubmodule allo = (AllomancySpiritwebSubmodule) ((SpiritwebCapability) data).getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY);

		int ingestedMetalAmount = allo.getIngestedMetal(metalType);

		if (ingestedMetalAmount > 0)
		{
			final int amountToAdjust = ingestedMetalAmount > 30 ? (ingestedMetalAmount / 2) : ingestedMetalAmount;
			allo.adjustIngestedMetal(
					metalType,
					-amountToAdjust, //take the amount away
					true);
			return true;
		}

		return false;
	}


}
