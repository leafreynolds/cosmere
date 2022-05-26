/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.allomancy;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.MobEffectBase;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AllomancyBoostEffect extends MobEffectBase
{
	public AllomancyBoostEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(effectType, type.getColorValue());

		AttributesRegistry.COSMERE_ATTRIBUTES.entrySet()
				.forEach(attributeRegistered ->
				{
					//todo powers to NOT increase?
					//skip duralumin and nicrosil?

					addAttributeModifier(
							attributeRegistered.getValue().get(),
							"ad9ba05c-d9e5-4f74-8f25-fa65139d178c",
							0.334D,
							AttributeModifier.Operation.MULTIPLY_TOTAL);
				});
	}

	@Override
	public void applyEffectTick(LivingEntity livingEntity, int amplifier)
	{
		//todo boost metal drain balancing
		//4 times a second?
		boolean isActiveTick = livingEntity.tickCount % 5 == 0;

		if (isActiveTick)
		{
			//todo change the allomancy boost effect to be the one that drains the metals.
			//just run the duralumin effect on the other entity
			SpiritwebCapability.get(livingEntity).ifPresent(data ->
					{
						//drain metals that are actively being burned
						for (Metals.MetalType metalType : Metals.MetalType.values())
						{
							if (!metalType.hasAssociatedManifestation())
							{
								continue;
							}

							//if metal is active
							if (data.canTickManifestation(Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalType.getID())))
							{
								int ingestedMetalAmount = data.getIngestedMetal(metalType);

								if (ingestedMetalAmount > 0)
								{
									if (ingestedMetalAmount > 27)
									{
										data.adjustIngestedMetal(metalType, 27, true);
									}
									else
									{
										data.adjustIngestedMetal(metalType, ingestedMetalAmount, true);
									}
								}
							}
						}

					}
			);


		}

	}
}
