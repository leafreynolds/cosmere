/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class SurgeAbrasion extends SurgebindingManifestation
{
	public SurgeAbrasion(Roshar.Surges surge)
	{
		super(surge);
	}

	@Override
	public int modeMin(ISpiritweb data)
	{
		SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
		return -submodule.getIdeal();
	}

	@Override
	public boolean tick(ISpiritweb data)
	{
		if (!isActive(data))
		{
			return false;
		}
		int mode = getMode(data);
		LivingEntity livingEntity = data.getLiving();
		SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
		if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.ABRASION).getManifestation() instanceof SurgebindingManifestation sg &&
				sg.isActive(data))
		{
			if (mode > 0 && submodule.adjustStormlight(-4, true))
			{
				livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SPEED, mode - 1, 2));
			}
			if (mode < 0 && submodule.adjustStormlight(-4, true))
			{
				livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SLOWDOWN, -mode - 1, 2));
			}
		}
		return super.tick(data);
	}
}
