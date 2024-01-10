package leaf.cosmere.allomancy.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class BrassStunEffect extends MobEffect
{
	public BrassStunEffect(MobEffectCategory category, int color)
	{
		super(category, color);
	}

	@Override
	public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier)
	{
		if (pLivingEntity instanceof Mob mob)
		{
			mob.setNoAi(true);
		}
		super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier)
	{
		if (pLivingEntity instanceof Mob mob)
		{
			mob.setNoAi(false);
		}
		super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
	}
}
