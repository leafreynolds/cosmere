/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;


public class IronTapEffect extends FeruchemyEffectBase
{
	public IronTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		addAttributeModifier(
				Attributes.KNOCKBACK_RESISTANCE,
				"bb29d10a-c58f-4f7e-956b-133b2685831f",
				0.1D,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(
				ForgeMod.ENTITY_GRAVITY.get(),
				"89499e2b-8797-4473-89c4-541aa703f17f",
				0.01D,
				AttributeModifier.Operation.ADDITION);
	}
}
