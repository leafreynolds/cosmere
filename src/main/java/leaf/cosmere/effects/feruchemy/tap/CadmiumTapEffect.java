/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.tap;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;


public class CadmiumTapEffect extends FeruchemyEffectBase
{
	public CadmiumTapEffect(Metals.MetalType type, MobEffectCategory effectType)
	{
		super(type, effectType);
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
