/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
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
