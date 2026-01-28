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
		LivingEntity target = event.getEntity();
		LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
		if (!attacker.getMainHandItem().isEmpty())
		{
			return;
		}

		SpiritwebCapability.get(attacker).ifPresent(iSpiritweb ->
		{
			if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.ADHESION).get()) && SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.ADHESION).getManifestation().isActive(iSpiritweb))
			{
				SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) iSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

				if (submodule.adjustStormlight(-40, true))
				{
					target.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SLOWDOWN, 50));
					target.setJumping(false);
				}
			}
		});
	}
}
