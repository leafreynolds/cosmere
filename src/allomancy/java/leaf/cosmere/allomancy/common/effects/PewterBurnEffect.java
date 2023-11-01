/*
 * File updated ~ 2 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.effects;

import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class PewterBurnEffect extends CosmereEffect
{
	public PewterBurnEffect()
	{
		super();

		//equivalent to a speed potion for misting of 8 strength, double for flaring.
		addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
				0.0125D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

		//equivalent to a haste potion for misting of 8 strength, double for flaring.
		addAttributeModifier(
				Attributes.ATTACK_SPEED,
				0.0125D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

		//3 damage at 8 strength, double for flare
		addAttributeModifier(
				Attributes.ATTACK_DAMAGE,
				0.375D,
				AttributeModifier.Operation.ADDITION);

		//3 knock back at 8 strength, double for flare
		addAttributeModifier(
				Attributes.ATTACK_KNOCKBACK,
				0.03125D,
				AttributeModifier.Operation.ADDITION);

		//damage resistance
		addAttributeModifier(
				AttributesRegistry.DETERMINATION.get(),//please forgive me for my sins anime god
				0.125D,
				AttributeModifier.Operation.ADDITION);

	}

}
