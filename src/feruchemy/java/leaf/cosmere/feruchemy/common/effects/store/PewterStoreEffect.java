/*
 * File updated ~ 12 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class PewterStoreEffect extends FeruchemyEffectBase
{
	public PewterStoreEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				Attributes.ATTACK_DAMAGE,
				-1.0D,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(
				Attributes.ATTACK_KNOCKBACK,
				-1.0D,
				AttributeModifier.Operation.ADDITION);
	}
}
