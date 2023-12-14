/*
 * File updated ~ 12 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class SteelStoreEffect extends FeruchemyEffectBase
{
	public SteelStoreEffect(Metals.MetalType type)
	{
		super(type);

		this.addAttributeModifier(
				Attributes.ATTACK_SPEED,
				-0.1F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

		this.addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
				-0.15F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
