/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;

public class AllomancyAluminum extends AllomancyBase
{
	public AllomancyAluminum(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		//passive active ability, if any
		if (data.getLiving().tickCount % 20 == 0)
		{
			int drainedCount = 0;

			//drain all metals
			for (Metals.MetalType metalType : Metals.MetalType.values())
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
		int ingestedMetalAmount = data.getIngestedMetal(metalType);

		if (ingestedMetalAmount > 0)
		{
			data.adjustIngestedMetal(
					metalType,
					ingestedMetalAmount > 30 ? (ingestedMetalAmount / 2) : ingestedMetalAmount,
					true);
			return true;
		}

		return false;
	}


}
