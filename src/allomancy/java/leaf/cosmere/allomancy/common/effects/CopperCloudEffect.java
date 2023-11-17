/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.effects;

import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class CopperCloudEffect extends CosmereEffect
{
	public CopperCloudEffect()
	{
		super();
		addAttributeModifier(
				AttributesRegistry.COGNITIVE_CONCEALMENT.get(),
				1,
				AttributeModifier.Operation.ADDITION);
	}
}
