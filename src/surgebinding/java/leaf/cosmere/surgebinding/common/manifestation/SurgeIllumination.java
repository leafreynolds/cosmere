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

public class SurgeIllumination extends SurgebindingManifestation
{
	public SurgeIllumination(Roshar.Surges surge)
	{
		super(surge);
	}


	//illusions
	//Also Light


	@Override
	public boolean tick(ISpiritweb data)
	{
		if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.ILLUMINATION).getManifestation() instanceof SurgebindingManifestation sg &&
				sg.isActive(data))
		{
			SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
			if (submodule.adjustStormlight(-2, true))
			{
				data.getLiving().addEffect(EffectsHelper.getNewEffect(MobEffects.NIGHT_VISION, 9, 60));
			}
		}

		return super.tick(data);
	}
}
