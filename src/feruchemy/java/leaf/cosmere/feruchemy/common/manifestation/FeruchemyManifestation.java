/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.charge.MetalmindChargeHelper;
import leaf.cosmere.feruchemy.common.registries.FeruchemyEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FeruchemyManifestation extends Manifestation implements IHasMetalType
{
	protected final Metals.MetalType metalType;

	public FeruchemyManifestation(Metals.MetalType metalType)
	{
		super(Manifestations.ManifestationTypes.FERUCHEMY);
		this.metalType = metalType;
	}

	@Override
	public int getPowerID()
	{
		return metalType.getID();
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}

	@Override
	public boolean modeWraps(ISpiritweb data)
	{
		return false;
	}

	//storing is positive, eg adding to store
	@Override
	public int modeMax(ISpiritweb data)
	{
		final double strength = getStrength(data, false);
		return Mth.fastFloor(strength / 3);
	}

	//tapping is negative, eg taking from store
	@Override
	public int modeMin(ISpiritweb data)
	{
		final double strength = getStrength(data, false);
		return -(Mth.fastFloor(strength));
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		super.onModeChange(data, lastMode);

		if (getMode(data) == 0)
		{
			//todo check if removing effects on mode change is wise. May be better to let them run out as they have already "paid" for them.
			final LivingEntity effectSource = data.getLiving();
			data.removeEffect(EffectsHelper.getEffectUUID(getStoringEffect(), effectSource));
			data.removeEffect(EffectsHelper.getEffectUUID(getTappingEffect(), effectSource));
		}
	}

	protected CosmereEffect getTappingEffect()
	{
		return FeruchemyEffects.TAPPING_EFFECTS.get(this.metalType).get();
	}

	protected CosmereEffect getStoringEffect()
	{
		return FeruchemyEffects.STORING_EFFECTS.get(this.metalType).get();
	}

	public boolean isStoring(ISpiritweb data)
	{
		return getMode(data) > 0;
	}

	public boolean isTapping(ISpiritweb data)
	{
		return getMode(data) < 0;
	}

	public boolean canAfford(ISpiritweb data, boolean simulate)
	{
		int adjustAmount = getCost(data);
		final ItemStack metalmind = MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, adjustAmount, !simulate, true);

		if (!metalmind.isEmpty())
		{
			return true;
		}

		if (!simulate)
		{
			final int mode = getMode(data);
			if (mode < 0)
			{
				//move towards turning off feruchemy.
				data.setMode(this, mode + 1);
			}
		}

		return false;
	}

	public int getCost(ISpiritweb data)
	{
		int mode = data.getMode(this);

		// if we are tapping
		//check if there is charges to tap
		if (mode < 0)
		{
			//wanting to tap
			//get cost
			return mode >= -modeMax(data) ? mode : -(Mth.absFloor(Math.pow(Mth.abs(mode), 1.5d)));
		}
		//if we are storing
		//check if there is space to store
		else if (mode > 0)
		{
			return mode;
		}
		return 0;
	}


	@Override
	public boolean tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		int mode = getMode(data);

		if ((livingEntity.tickCount % 20 != 0) || mode == 0)
		{
			//if not active tick, or mode is off
			return false;
		}

		if (canAfford(data, false))//success
		{
			applyEffectTick(data);
			//todo, move to config so that players can choose how high tap rate needs to be to be picked up by sculk
			return isTapping(data) && mode < -4;
		}
		return false;
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);
		CosmereEffect effect = getEffect(mode);
		CosmereEffectInstance currentEffect = EffectsHelper.getNewEffect(effect, data.getLiving(), Math.abs(mode) - 1);//todo check this strength

		data.addEffect(currentEffect);
	}

	protected CosmereEffect getEffect(int mode)
	{
		if (mode == 0)
		{
			return null;
		}
		else if (mode < 0)
		{
			return getTappingEffect();
		}
		else
		{
			return getStoringEffect();
		}

	}

}
