/*
 * File updated ~ 21 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

//protagonist energy
public class ElectrumStoreEffect extends FeruchemyEffectBase
{
	public ElectrumStoreEffect(Metals.MetalType type)
	{
		super(type);

		addAttributeModifier(
				AttributesRegistry.DETERMINATION.getAttribute(),
				-1.0D,
				AttributeModifier.Operation.ADDITION);
	}
}
