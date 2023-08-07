package leaf.cosmere.sandmastery.common.effects;

import leaf.cosmere.common.effects.MobEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DehydratedEffect extends MobEffectBase
{
	public DehydratedEffect(MobEffectCategory mobEffectCategory, int colorValue)
	{
		super(mobEffectCategory, colorValue);
	}

	@Override
	public void applyEffectTick(LivingEntity livingEntity, int amplifier)
	{

	}
}
