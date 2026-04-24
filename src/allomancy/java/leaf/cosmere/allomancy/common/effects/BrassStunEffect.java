package leaf.cosmere.allomancy.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class BrassStunEffect extends MobEffect
{
	public BrassStunEffect(MobEffectCategory category, int color)
	{
		super(category, color);
	}

	// 1.21.1: addAttributeModifiers no longer receives LivingEntity, so the noAi toggle
	// moved to onEffectAdded (set) and a paired MobEffectEvent.Remove/Expired handler
	// in AllomancyEntityEventHandler (clear) — see clearStun below.
	@Override
	public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier)
	{
		super.onEffectAdded(pLivingEntity, pAmplifier);
		if (pLivingEntity instanceof Mob mob)
		{
			mob.setNoAi(true);
		}
	}

	public static void clearStun(LivingEntity living)
	{
		if (living instanceof Mob mob)
		{
			mob.setNoAi(false);
		}
	}
}
