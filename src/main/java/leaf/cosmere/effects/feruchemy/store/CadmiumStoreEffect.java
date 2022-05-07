/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.effects.feruchemy.store;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

// air
public class CadmiumStoreEffect extends FeruchemyEffectBase
{
	public CadmiumStoreEffect(Metals.MetalType type, MobEffectCategory effectType)
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

		entityLivingBaseIn.setAirSupply(Mth.clamp(entityLivingBaseIn.getAirSupply() - 4 - (amplifier), -20, entityLivingBaseIn.getMaxAirSupply()));

		if (entityLivingBaseIn.getAirSupply() < -10 && entityLivingBaseIn.tickCount % 50 == 0)
		{
			entityLivingBaseIn.hurt(DamageSource.DROWN, 2.0F);
		}
	}
}
