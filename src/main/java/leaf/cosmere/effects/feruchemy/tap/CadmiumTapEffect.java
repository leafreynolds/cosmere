/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class CadmiumTapEffect extends FeruchemyEffectBase
{
	public CadmiumTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);

		this.addAttributeModifier(
				Attributes.ATTACK_SPEED,
				"019c1754-d7a2-4c78-9e14-896ecc7ed0e2",
				0.01F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

		this.addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
				"019c1754-d7a2-4c78-9e14-896ecc7ed0e2",
				0.02F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
	{
		if (entityLivingBaseIn.level.isClientSide)
		{
			return;
		}

		entityLivingBaseIn.setAirSupply(Mth.clamp(entityLivingBaseIn.getAirSupply() + 3 + (amplifier), entityLivingBaseIn.getAirSupply(), entityLivingBaseIn.getMaxAirSupply()));

	}
}
