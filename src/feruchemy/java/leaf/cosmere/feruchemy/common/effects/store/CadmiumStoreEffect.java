/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects.store;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.feruchemy.common.effects.FeruchemyEffectBase;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
		if (living.level().isClientSide)
		{
			return;
		}

		final int minAirSupply = -20;
		final int maxAirSupply = living.getMaxAirSupply();
		final double potentialNextVal = living.getAirSupply() - 4 - (strength);

		if (strength >= 0.0)
		{
			// set to just low enough that bubbles appear in the hud, or potentialNextVal, whichever is lower
			living.setAirSupply((int) Math.min(potentialNextVal, maxAirSupply-10));
		}
		else
		{
			living.setAirSupply((int) Mth.clamp(potentialNextVal, minAirSupply, maxAirSupply));
		}

		//every 2.5 seconds
		if (living.getAirSupply() < -10 && (getTickToCheck(data) % getActiveTick() == 0))
		{
			living.hurt(new DamageSource(living.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.DROWN)), 2.0F);
		}
	}
}
