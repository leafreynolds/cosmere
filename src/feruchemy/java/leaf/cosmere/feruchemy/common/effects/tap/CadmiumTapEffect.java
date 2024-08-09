/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.tap;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
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
	protected boolean isActiveTick(ISpiritweb data)
	{
		//just make cadmium always run effect tick.
		return true;
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		final LivingEntity living = data.getLiving();
		if (living.level().isClientSide)
		{
			return;
		}
		final int minAirSupply = living.getAirSupply();
		final int maxAirSupply = living.getMaxAirSupply();
		final double potentialNextVal = minAirSupply + 3 + (strength);//todo find out what 3 means here.
		living.setAirSupply((int) Mth.clamp(potentialNextVal, minAirSupply, maxAirSupply));

	}
}
