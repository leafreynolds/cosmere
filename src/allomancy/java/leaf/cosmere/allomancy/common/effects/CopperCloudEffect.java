/*
 * File updated ~ 7 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.effects;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.effects.MobEffectBase;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class CopperCloudEffect extends MobEffectBase
{
	public CopperCloudEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(effectType, type.getColorValue());
		addAttributeModifier(
				AttributesRegistry.COGNITIVE_CONCEALMENT.get(),
				"39194b59-b3e7-4978-9e37-6b19a104eabe",
				1,
				AttributeModifier.Operation.ADDITION);
	}
}
