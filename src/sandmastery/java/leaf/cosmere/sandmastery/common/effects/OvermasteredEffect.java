/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery.common.effects;

import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class OvermasteredEffect extends CosmereEffect
{
	public OvermasteredEffect()
	{
		super();

		addAttributeModifier(
				BuiltInRegistries.ATTRIBUTE.wrapAsHolder(SandmasteryAttributes.RIBBONS.get()),
				-1000d,
				AttributeModifier.Operation.ADD_VALUE);
	}
}
