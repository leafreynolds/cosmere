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

	int counterToDamage = 0;

	@Override
	public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier)
	{
		counterToDamage++;
		if (counterToDamage == 100)
		{
			pLivingEntity.setHealth(pLivingEntity.getHealth() - 1);
			counterToDamage = 0;
		}
		super.applyEffectTick(pLivingEntity, pAmplifier);
	}
}
