/*
 * File updated ~ 7 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

// Honors truest surge
public class SurgeAdhesion extends SurgebindingManifestation
{
	public SurgeAdhesion(Roshar.Surges surge)
	{
		super(surge);
	}

	//bind things together

	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		if (!(event.getSource().getEntity() instanceof LivingEntity attacker))
		{
			return;
		}
		LivingEntity target = event.getEntity();
		SpiritwebCapability.get(attacker).ifPresent(iSpiritweb ->
		{
			if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.ADHESION).getManifestation() instanceof SurgebindingManifestation sg &&
					sg.isActive(iSpiritweb))
			{
				MobEffectInstance slowedEffect = target.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
				if (slowedEffect != null && slowedEffect.getAmplifier() == 50)
				{
					if (submodule.adjustStormlight(slowedEffect.getDuration() / 20, true))
					{
						target.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
					}
				}
				else
				{
					if (submodule.adjustStormlight(-60, true))
					{
						target.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SLOWDOWN, 50, 1200));
					}
				}
			}
		});
	}

}
