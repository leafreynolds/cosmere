/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyCopper extends AllomancyBase
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
			MobEffectInstance newEffect = EffectsHelper.getNewEffect(
					EffectsRegistry.ALLOMANTIC_COPPER.get(),
					Mth.fastFloor(
							getStrength(data, false)
					)
			);

			switch (getMode(data))
			{
				case 1:
					data.getLiving().addEffect(newEffect);
					break;
				case 2:
				case 3:
					List<LivingEntity> entitiesToApplyEffect = getLivingEntitiesInRange(livingEntity, getRange(data), true);

					for (LivingEntity e : entitiesToApplyEffect)
					{
						e.addEffect(newEffect);
					}
					break;
			}

		}
	}
}
