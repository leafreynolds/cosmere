/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.surgebinding.common.registries.SurgebindingEffects;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
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
		LivingEntity living = data.getLiving();
		if(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.TENSION).getManifestation().isActive(data))
			data.getLiving().addEffect(EffectsHelper.getNewEffect(SurgebindingEffects.RIGID_DEFENSE.getMobEffect(), 9));
		return super.tick(data);
	}
}
