/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.effects;

import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.allomancy.common.registries.AllomancyAttributes;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AllomancyBoostEffect extends CosmereEffect
{
	public AllomancyBoostEffect()
	{
		super();

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasAssociatedManifestation() && AllomancyAttributes.ALLOMANCY_ATTRIBUTES.containsKey(metalType))
			{
				addAttributeModifier(
						AllomancyAttributes.ALLOMANCY_ATTRIBUTES.get(metalType).get(),
						0.334D,// todo config - Need to figure out an alternative to config boost amount //AllomancyConfigs.SERVER.boostAmount.get(),
						AttributeModifier.Operation.MULTIPLY_TOTAL);
				//todo boost other manifestation types

			}
		}
		//todo boost other manifestation types

	}

	@Override
	protected int getTickOffset()
	{
		return Metals.MetalType.NICROSIL.getID();
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		//todo boost metal drain balancing

		AllomancySpiritwebSubmodule allo = AllomancySpiritwebSubmodule.getSubmodule(data);

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasAssociatedManifestation() || metalType == Metals.MetalType.DURALUMIN)
			{
				continue;
			}

			int ingestedMetalAmount = allo.getIngestedMetal(metalType);

			//if metal exists
			if (ingestedMetalAmount > 0)
			{
				//drain metals that are actively being burned
				if (data.canTickManifestation(Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalType.getID())))
				{
					final int amountToAdjust =
							ingestedMetalAmount > 30 ? (ingestedMetalAmount / 2) : ingestedMetalAmount;
					allo.adjustIngestedMetal(
							metalType,
							-amountToAdjust, //take amount away
							true);

				}
			}
		}
	}
}
