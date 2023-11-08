/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
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
	protected boolean isActiveTick(ISpiritweb data)
	{
		//just make cadmium always run effect tick.
		return true;
	}

	@Override
	protected int getActiveTick()
	{
		return 50;
	}

	@Override
	public void applyEffectTick(ISpiritweb data, double strength)
	{
		final LivingEntity living = data.getLiving();
		if (living.level.isClientSide)
		{
			return;
		}

		final int minAirSupply = -20; //entityLivingBaseIn.getAirSupply();
		final int maxAirSupply = living.getMaxAirSupply();
		final double potentialNextVal = minAirSupply - 4 - (strength);
		living.setAirSupply((int) Mth.clamp(potentialNextVal, minAirSupply, maxAirSupply));

		//every 2.5 seconds
		if (living.getAirSupply() < -10 && (getTickToCheck(data) % getActiveTick() == 0))
		{
			living.hurt(DamageSource.DROWN, 2.0F);
		}
	}
}
