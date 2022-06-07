/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;


public class ZincTapEffect extends FeruchemyEffectBase
{
	public ZincTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
		addAttributeModifier(
				AttributesRegistry.COSMERE_ATTRIBUTES.get(Metals.MetalType.COPPER.getName()).get(),
				"1a7c0d6e-d206-4939-ae07-b47783656f2d",
				0.1D,
				AttributeModifier.Operation.ADDITION);
	}
}
