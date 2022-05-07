/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class SteelStoreEffect extends FeruchemyEffectBase
{
	public SteelStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		this.addAttributeModifier(
				Attributes.ATTACK_SPEED,
				"bc7ff64b-f90c-49c0-81e1-6a6df3a3e612",
				-0.1F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

		this.addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
				"52583b16-7124-4443-b4e8-1497d6c793f2",
				-0.15F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
