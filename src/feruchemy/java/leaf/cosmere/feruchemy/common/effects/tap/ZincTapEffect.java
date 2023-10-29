/*
 * File updated ~ 12 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


public class ZincTapEffect extends FeruchemyEffectBase
{
	public ZincTapEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				AttributesRegistry.XP_RATE_ATTRIBUTE.get(),
				0.15D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
