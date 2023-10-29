/*
 * File updated ~ 27 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AllomancyCopper extends AllomancyManifestation
{
	public AllomancyCopper(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int modeMax(ISpiritweb data)
	{
		//1 for affecting self
		//2 for cloud effect
		//3 for flaring

		return 3;
	}

	@Override
	public int getRange(ISpiritweb data)
	{
		if (!isActive(data))
		{
			return 0;
		}

		//get allomantic strength
		double allomanticStrength = getStrength(data, false);

		//mode minus one, because copper has special mode stuff.
		final int mode = getMode(data);
		int i = switch (mode)
				{
					case 1, 2 -> 1;
					case 3 -> 2;
					default -> 0;
				};

		return Mth.floor(allomanticStrength * i);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;

		//Hides Allomantic Pulses
		if (isActiveTick)
		{

			final int actionableStrength = Mth.fastFloor(getStrength(data, false));
			switch (getMode(data))
			{
				case 1:
					data.addEffect(
							EffectsHelper.getNewEffect(
									AllomancyEffects.ALLOMANTIC_COPPER.get(),
									data.getLiving(),
									actionableStrength
							));
					break;
				case 2:
				case 3:
					List<LivingEntity> entitiesToApplyEffect = EntityHelper.getLivingEntitiesInRange(livingEntity, getRange(data), true);

					for (LivingEntity target : entitiesToApplyEffect)
					{
						SpiritwebCapability.get(target).ifPresent(targetSpiritweb ->
						{
							targetSpiritweb.addEffect(EffectsHelper.getNewEffect(
									AllomancyEffects.ALLOMANTIC_COPPER.get(),
									data.getLiving(),
									actionableStrength
							));

						});
					}
					break;
			}

		}
	}
}
