/*
 * File updated ~ 12 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;


public class IronTapEffect extends FeruchemyEffectBase
{
	public IronTapEffect(Metals.MetalType type)
	{
		super(type);
		addAttributeModifier(
				Attributes.KNOCKBACK_RESISTANCE,
				0.1D,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(
				ForgeMod.ENTITY_GRAVITY.get(),
				0.01D,
				AttributeModifier.Operation.ADDITION);
	}
}
