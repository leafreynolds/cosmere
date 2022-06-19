/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.MetalmindChargeHelper;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FeruchemyGold extends FeruchemyBase
{
	public FeruchemyGold(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int getCost(ISpiritweb data)
	{
		final int cost = super.getCost(data);
		//todo config gold tap cost
		return isTapping(data) ? cost * 10 : cost;
	}

	@Override
	public void tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		//don't heal more than needed.
		if (livingEntity.tickCount % 20 != 0)
		{
			return;
		}

		if (isTapping(data) && livingEntity.getHealth() >= livingEntity.getMaxHealth())
			return;

		super.tick(data);
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		super.applyEffectTick(data);
		final int mode = getMode(data);
		MobEffect effect = getEffect(mode);
		MobEffectInstance currentEffect = EffectsHelper.getNewEffect(effect, Math.abs(mode) - 1);

		if (effect == null)
		{
			return;
		}

		data.getLiving().addEffect(currentEffect);
	}
}
