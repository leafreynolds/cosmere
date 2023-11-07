/*
 * File updated ~ 23 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class CadmiumTapEffect extends FeruchemyEffectBase
{
	public CadmiumTapEffect(Metals.MetalType type)
	{
		super(type);

		this.addAttributeModifier(
				Attributes.ATTACK_SPEED,
				0.01F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);

		this.addAttributeModifier(
				Attributes.MOVEMENT_SPEED,
				0.02F,
				AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, double strength)
	{
		if (entityLivingBaseIn.level.isClientSide)
		{
			return;
		}
		final int minAirSupply = entityLivingBaseIn.getAirSupply();
		final int maxAirSupply = entityLivingBaseIn.getMaxAirSupply();
		final double potentialNextVal = minAirSupply + 3 + (strength);//todo find out what 3 means here.
		entityLivingBaseIn.setAirSupply((int) Mth.clamp(potentialNextVal, minAirSupply, maxAirSupply));

	}
}
