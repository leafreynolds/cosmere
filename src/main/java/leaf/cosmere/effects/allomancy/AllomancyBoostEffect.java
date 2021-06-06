/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.allomancy;

import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class AllomancyBoostEffect extends Effect
{
    public AllomancyBoostEffect(Metals.MetalType type, EffectType effectType)
    {
        super(effectType, type.getColorValue());

        AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.entrySet()
                .forEach(attributeRegistered ->
                {
                    //todo powers to NOT increase?
                    //skip duralumin and nicrosil?

                    addAttributesModifier(
                            attributeRegistered.getValue().get(),
                            "ad9ba05c-d9e5-4f74-8f25-fa65139d178c",
                            0.334D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL);
                });
    }

    @Override
    public void performEffect(LivingEntity livingEntity, int amplifier)
    {
        //todo boost metal drain balancing
        //4 times a second?
        boolean isActiveTick = livingEntity.ticksExisted % 5 == 0;

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
                            if (data.canTickManifestation(Manifestations.ManifestationTypes.ALLOMANCY, metalType.getID()))
                            {
                                int ingestedMetalAmount = data.getIngestedMetal(metalType);

                                if (ingestedMetalAmount > 0)
                                {
                                    if (ingestedMetalAmount >= 27)
                                    {
                                        data.adjustIngestedMetal(metalType, ingestedMetalAmount - 27, true);
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
