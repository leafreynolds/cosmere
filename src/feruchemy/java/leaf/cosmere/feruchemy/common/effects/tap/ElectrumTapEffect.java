/*
 * File updated ~ 23 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


public class ElectrumTapEffect extends FeruchemyEffectBase
{
	public ElectrumTapEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				AttributesRegistry.DETERMINATION.getAttribute(),
				1.0D,
				AttributeModifier.Operation.ADDITION);
	}
}
