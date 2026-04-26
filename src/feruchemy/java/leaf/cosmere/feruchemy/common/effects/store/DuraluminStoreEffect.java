/*
 * File updated ~ 23 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

//connection aka ability for people to notice you
public class DuraluminStoreEffect extends FeruchemyEffectBase
{
	public DuraluminStoreEffect(Metals.MetalType type)
	{
		super(type);

		addAttributeModifier(
				AttributesRegistry.CONNECTION.getAttribute(),
				-1.0D,
				AttributeModifier.Operation.ADD_VALUE);

		// TODO: LivingVisibilityEvent was removed in NeoForge 1.21.1.
		// Re-implement visibility reduction via a Mixin into getVisibilityPercent.
	}
}
