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
	public void tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		//don't heal more than needed.
		if (livingEntity.tickCount % 20 != 0)
		{
			return;
		}

		int mode = getMode(data);

		int cost;

		MobEffect effect = getEffect(mode);
		final boolean tapping = mode < 0;
		final boolean storing = mode > 0;

		// if we are tapping
		//check if there is charges to tap
		if (tapping)
		{
			//wanting to tap
			//get cost
			cost = mode <= -3 ? -(mode * mode) : mode;

		}
		//if we are storing
		//check if there is space to store
		else
		{
			if (storing)
			{
				cost = mode;
			}
			//can't store or tap any more
			else
			{
				//remove active effects.
				//let the current effect run out.
				return;
			}
		}

		if (tapping && livingEntity.getHealth() >= livingEntity.getMaxHealth())
			return;

		final ItemStack metalmind = MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, -cost, true, true);
		if (metalmind != null)//success
		{
			MobEffectInstance currentEffect = EffectsHelper.getNewEffect(effect, Math.abs(mode) - 1);

			if (effect == null)
			{
				return;
			}

			livingEntity.addEffect(currentEffect);
		}
		else
		{
			if (tapping)
			{
				//move towards turning off feruchemy.
				data.setMode(this, mode + 1);
			}
		}
	}
}
