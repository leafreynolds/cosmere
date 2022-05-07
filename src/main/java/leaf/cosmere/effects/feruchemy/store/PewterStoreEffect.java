/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class PewterStoreEffect extends FeruchemyEffectBase
{
	public PewterStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		addAttributeModifier(
				Attributes.ATTACK_DAMAGE,
				"e8f5c2b2-f724-4514-82b8-9d10be304c42",
				-1.0D,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(
				Attributes.ATTACK_KNOCKBACK,
				"cb83254e-f9c4-4d67-9976-f2fdf69061ce",
				-1.0D,
				AttributeModifier.Operation.ADDITION);
	}
}
