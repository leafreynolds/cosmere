/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.effects.MobEffectBase;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AllomancyBoostEffect extends MobEffectBase
{
	public AllomancyBoostEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(effectType, type.getColorValue());

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasAssociatedManifestation())
			{
				addAttributeModifier(
						AttributesRegistry.ALLOMANCY_ATTRIBUTES.get(metalType).get(),
						"ad9ba05c-d9e5-4f74-8f25-fa65139d178c",
						0.334D,
						AttributeModifier.Operation.MULTIPLY_TOTAL);
				addAttributeModifier(
						AttributesRegistry.FERUCHEMY_ATTRIBUTES.get(metalType).get(),
						"ad9ba05c-d9e5-4f74-8f25-fa65139d178c",
						0.334D,
						AttributeModifier.Operation.MULTIPLY_TOTAL);

			}
		}

		for (Roshar.Surges surges : Roshar.Surges.values())
		{
			addAttributeModifier(
					AttributesRegistry.SURGEBINDING_ATTRIBUTES.get(surges).get(),
					"ad9ba05c-d9e5-4f74-8f25-fa65139d178c",
					0.334D,
					AttributeModifier.Operation.MULTIPLY_TOTAL);
		}

	}

	@Override
	public void applyEffectTick(LivingEntity livingEntity, int amplifier)
	{
		//todo boost metal drain balancing

		if (isActiveTick(livingEntity))
		{
			SpiritwebCapability.get(livingEntity).ifPresent(data ->
			{
				for (Metals.MetalType metalType : Metals.MetalType.values())
				{
					if (!metalType.hasAssociatedManifestation())
					{
						continue;
					}

					int ingestedMetalAmount = data.getIngestedMetal(metalType);

					//if metal exists
					if (ingestedMetalAmount > 0)
					{
						//drain metals that are actively being burned
						if (data.canTickManifestation(Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalType.getID())))
						{
							drainMetal(data, metalType);
						}
					}
				}
			});
		}
	}

	private boolean drainMetal(ISpiritweb data, Metals.MetalType metalType)
	{
		int ingestedMetalAmount = data.getIngestedMetal(metalType);

		if (ingestedMetalAmount > 0)
		{
			final int amountToAdjust = ingestedMetalAmount > 30 ? (ingestedMetalAmount / 2) : ingestedMetalAmount;
			data.adjustIngestedMetal(
					metalType,
					-amountToAdjust, //take amount away
					true);
			return true;
		}

		return false;
	}
}
