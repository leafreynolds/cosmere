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
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

// Honors truest surge
public class SurgeAdhesion extends SurgebindingManifestation
{
	public SurgeAdhesion(Roshar.Surges surge)
	{
		super(surge);
	}

	//bind things together
	public static void onEntiityInteract(PlayerInteractEvent.EntityInteract event){
		LivingEntity target = (LivingEntity) event.getTarget();
		SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
		{
			if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.ADHESION).get()) && SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.ADHESION).getManifestation().isActive(iSpiritweb))
			{
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

				if (submodule.adjustStormlight(-60, true))
				{
					target.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SLOWDOWN, 50, 1200));
				}
			}
		});
		MobEffectInstance slowedEffect = target.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
		if(slowedEffect!= null && slowedEffect.getAmplifier()==50)
		{

		}
	}
}
