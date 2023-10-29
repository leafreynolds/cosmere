/*
 * File updated ~ 23 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

// air
public class CadmiumStoreEffect extends FeruchemyEffectBase
{
	public CadmiumStoreEffect(Metals.MetalType type)
	{
		super(type);
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, double strength)
	{
		if (entityLivingBaseIn.level.isClientSide)
		{
			return;
		}

		final int minAirSupply = -20; //entityLivingBaseIn.getAirSupply();
		final int maxAirSupply = entityLivingBaseIn.getMaxAirSupply();
		final double potentialNextVal = minAirSupply - 4 - (strength);
		entityLivingBaseIn.setAirSupply((int) Mth.clamp(potentialNextVal, minAirSupply, maxAirSupply));

		if (entityLivingBaseIn.getAirSupply() < -10 && entityLivingBaseIn.tickCount % 50 == 0)
		{
			entityLivingBaseIn.hurt(DamageSource.DROWN, 2.0F);
		}
	}
}
