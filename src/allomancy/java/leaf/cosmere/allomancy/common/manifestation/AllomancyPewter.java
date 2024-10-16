/*
 * File updated ~ 15 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.allomancy.common.capabilities.AllomancySpiritwebSubmodule;
import leaf.cosmere.allomancy.common.registries.AllomancyDamageTypesRegistry;
import leaf.cosmere.allomancy.common.registries.AllomancyEffects;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

//Increases Physical Abilities
public class AllomancyPewter extends AllomancyManifestation
{
	public AllomancyPewter(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();

		//if we're here, we already know we are not compounding and have already 'paid' for the effect
		//if (!isCompounding(data))
		{
			final double strength = getStrength(data, false);
			//flaring gets extra out of the effect
			int actionableStrength = (int) Math.round(strength) * getMode(data);
			data.addEffect(EffectsHelper.getNewEffect(AllomancyEffects.ALLOMANTIC_PEWTER.getEffect(), livingEntity, actionableStrength));
		}

		AllomancySpiritwebSubmodule asm = AllomancySpiritwebSubmodule.getSubmodule(data);

		//todo come back to this, maybe configs, maybe fine tuning how often it reduces
		if (asm.getPewterDelayedDamage() > 0 && getActiveTick(data) % 1200 == 0)
		{
			asm.setPewterDelayedDamage(asm.getPewterDelayedDamage() - 1);
		}
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		super.onModeChange(data, lastMode);

		if (data.getLiving().level().isClientSide || data.getMode(this) > 0)
		{
			return;
		}

		data.removeEffect(EffectsHelper.getEffectUUID(AllomancyEffects.ALLOMANTIC_PEWTER.getEffect(), data.getLiving()));


		AllomancySpiritwebSubmodule asm = AllomancySpiritwebSubmodule.getSubmodule(data);
		float delayedDamage = asm.getPewterDelayedDamage();
		data.getLiving().hurt(AllomancyDamageTypesRegistry.damageSource(data.getLiving().level(), AllomancyDamageTypesRegistry.PEWTER_DELAYED_DAMAGE), delayedDamage);
		asm.setPewterDelayedDamage(0);

	}

	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		if (event.isCanceled())
		{
			return;
		}

		LivingEntity livingEntity = event.getEntity();

		SpiritwebCapability.get(livingEntity).ifPresent(data ->
		{
			AllomancyPewter pewter = (AllomancyPewter) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.PEWTER).get();
			if (pewter.isAllomanticBurn(data))
			{
				float damage = event.getAmount();
				//todo pewter damage reduction config
				//half by default?
				float damageReductionMultiplier = 0.5f;


				if (damage > livingEntity.getHealth() && pewter.isFlaring(data))
				{
					//prevent death by flaring
					damageReductionMultiplier = 0.1f;
				}


				final float newDamageAmount = damage * damageReductionMultiplier;
				final float delayedDamage = damage - newDamageAmount;

				event.setAmount(damage - newDamageAmount);
				AllomancySpiritwebSubmodule asm = (AllomancySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY);
				asm.setPewterDelayedDamage(asm.getPewterDelayedDamage() + delayedDamage);
			}

		});
	}
}
