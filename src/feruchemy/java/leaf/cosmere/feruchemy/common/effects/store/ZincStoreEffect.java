/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


public class ZincStoreEffect extends FeruchemyEffectBase
{
	public ZincStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		addAttributeModifier(
				AttributesRegistry.XP_RATE_ATTRIBUTE.get(),
				"9d50ed05-06f4-4bc2-83bc-d870f93696bf",
				-0.15D,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

}
