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


//storing all the available powers on the user individually
public class FeruchemyNicrosil extends FeruchemyBase
{
	public FeruchemyNicrosil(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		return -1;
	}

	@Override
	public int modeMax(ISpiritweb data)
	{
		return 1;
	}

	@Override
	public void tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		if (livingEntity.tickCount % 20 != 0)
		{
			return;
		}

		int mode = getMode(data);

		int cost;

		MobEffect effect = getEffect(mode);

		// if we are tapping
		//check if there is charges to tap
		if (mode < 0)
		{
			//wanting to tap
			//get cost
			cost = mode <= -3 ? -(mode * mode) : mode;

		}
		//if we are storing
		//check if there is space to store
		else if (mode > 0)
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

		if (MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, -cost, true, true))
		{
			MobEffectInstance currentEffect = EffectsHelper.getNewEffect(effect, Math.abs(mode) - 1);
			livingEntity.addEffect(currentEffect);
		}

	}

}
