/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

//health
public class AtiumTapEffect extends FeruchemyEffectBase
{
	public AtiumTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		addAttributeModifier(
				AttributesRegistry.COSMERE_ATTRIBUTES.get(Metals.MetalType.ATIUM.getName()).get(),
				"2c0b90c8-a8f1-4d83-9072-d77eb7e4b689",
				0.15D,
				AttributeModifier.Operation.ADDITION);
	}

}
