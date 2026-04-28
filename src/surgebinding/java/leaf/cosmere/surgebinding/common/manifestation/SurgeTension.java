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

public class SurgeTension extends SurgebindingManifestation
{
	public SurgeTension(Roshar.Surges surge)
	{
		super(surge);
	}


	//repair inanimate objects for bondsmiths?

	//making things rigid.

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
		if (SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TENSION).getManifestation() instanceof SurgebindingManifestation sg &&
				sg.isActive(data) &&
				livingEntity.isShiftKeyDown())
		{
			if (submodule.adjustStormlight(-2 * mode, true))
			{
				livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DAMAGE_RESISTANCE, mode - 1, 2));
			}
		}
		return super.tick(data);
	}
}
