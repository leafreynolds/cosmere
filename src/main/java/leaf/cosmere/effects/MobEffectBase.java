/*
 * File created ~ 26 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public abstract class MobEffectBase extends MobEffect
{
	protected MobEffectBase(MobEffectCategory mobEffectCategory, int colorValue)
	{
		super(mobEffectCategory, colorValue);
	}

	protected int getActiveTick()
	{
		return 20;
	}

	protected boolean isActiveTick(LivingEntity livingEntity)
	{
		return livingEntity.tickCount % getActiveTick() == 0;
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier)
	{
		//assume true, since we can't check the entity here
		return true;
	}
}
