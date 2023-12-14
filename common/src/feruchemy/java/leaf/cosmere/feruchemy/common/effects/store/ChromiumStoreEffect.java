/*
 * File updated ~ 23 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

//luck
public class ChromiumStoreEffect extends FeruchemyEffectBase
{
	public ChromiumStoreEffect(Metals.MetalType type)
	{
		super(type);

		addAttributeModifier(
				Attributes.LUCK,
				-1.0D,
				AttributeModifier.Operation.ADDITION);

		addAttributeModifier(
				AttributesRegistry.COSMERE_FORTUNE.getAttribute(),
				-1.0D,
				AttributeModifier.Operation.ADDITION);
	}
}
