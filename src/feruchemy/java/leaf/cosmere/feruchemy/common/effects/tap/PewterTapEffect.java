/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class PewterTapEffect extends FeruchemyEffectBase
{
	public PewterTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		addAttributeModifier(
				Attributes.ATTACK_DAMAGE,
				"00bebe52-fe9e-4966-989c-28de0cd2eb1f",
				1.0D,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(
				Attributes.ATTACK_KNOCKBACK,
				"74b5b82b-58e0-4a34-9cc2-fd4b92f0b11b",
				1.0D,
				AttributeModifier.Operation.ADDITION);
	}
}
