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

	//change frictional force

	@Override
	public int modeMin(ISpiritweb data) {return -1;}

	@Override
	public boolean tick(ISpiritweb data)
	{
		if(!isActive(data))
		{
			return false;
		}
		int mode = getMode(data);
		LivingEntity livingEntity = data.getLiving();
		SurgebindingSpiritwebSubmodule surg = (SurgebindingSpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
		if(data.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.ABRASION).getManifestation()) && SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.ABRASION).getManifestation().isActive(data))
		{
			if(surg.adjustStormlight(-mode,true))
			{
				if (mode>0) {livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SPEED, mode,2));}
				if (mode<0) {livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SLOWDOWN, mode,2));}
			}
		}
		return super.tick(data);
	}
}
