/*
 * File updated ~ 12 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class PewterTapEffect extends FeruchemyEffectBase
{
	public PewterTapEffect(Metals.MetalType type)
	{
		super(type);

		addAttributeModifier(
				Attributes.ATTACK_DAMAGE,
				1.0D,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(
				Attributes.ATTACK_KNOCKBACK,
				1.0D,
				AttributeModifier.Operation.ADDITION);
	}
}
