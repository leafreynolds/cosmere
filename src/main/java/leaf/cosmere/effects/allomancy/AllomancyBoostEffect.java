/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.allomancy;

import leaf.cosmere.constants.*;
import leaf.cosmere.items.*;
import leaf.cosmere.registry.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.potion.*;

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
}
