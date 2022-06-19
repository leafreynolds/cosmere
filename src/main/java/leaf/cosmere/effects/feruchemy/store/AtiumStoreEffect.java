/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

//health
public class AtiumStoreEffect extends FeruchemyEffectBase
{
	public AtiumStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		addAttributeModifier(
				AttributesRegistry.COSMERE_ATTRIBUTES.get(Metals.MetalType.ATIUM.getName()).get(),
				"69225c21-d36f-4ca3-8071-6b15279ca4f9",
				-0.15D,
				AttributeModifier.Operation.ADDITION);
	}

}
