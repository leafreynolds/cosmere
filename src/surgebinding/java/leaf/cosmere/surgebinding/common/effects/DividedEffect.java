package leaf.cosmere.surgebinding.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class DividedEffect extends MobEffect
{
	//In Development, Will try later.
	public DividedEffect(MobEffectCategory pCategory, int pColor)
	{
		super(pCategory, pColor);
	}

	@Override
	public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
	{
		pLivingEntity.hurt(pLivingEntity.damageSources().magic(), 1.0F);
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier)
	{
		int interval = 100 >> pAmplifier;
		return interval <= 0 || pDuration % interval == 0;
	}
}
