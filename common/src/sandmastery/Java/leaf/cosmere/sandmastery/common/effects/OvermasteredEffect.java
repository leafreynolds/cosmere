/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.effects;

import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class OvermasteredEffect extends CosmereEffect
{
	public OvermasteredEffect()
	{
		super();

		addAttributeModifier(SandmasteryAttributes.RIBBONS.get(), -1000d, AttributeModifier.Operation.ADDITION);
	}
}
