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

	@Override
	public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier)
	{
		if (pLivingEntity instanceof Mob mob)
		{
			mob.setNoAi(true);
		}
		super.onEffectAdded(pLivingEntity, pAmplifier);
	}

	// apparently Mojang made this super scuffed by adding an onEffectAdded method, but no remover, so you have to listen to events. I feel naught but hatred.
	public static void clearStun(LivingEntity living)
	{
		if (living instanceof Mob mob)
		{
			mob.setNoAi(false);
		}
	}
}
