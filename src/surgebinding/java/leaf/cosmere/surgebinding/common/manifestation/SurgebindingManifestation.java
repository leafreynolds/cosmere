/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class SurgebindingManifestation extends Manifestation
{
	protected final Roshar.Surges surge;

	public SurgebindingManifestation(Roshar.Surges surge)
	{
		super(Manifestations.ManifestationTypes.SURGEBINDING);
		this.surge = surge;
	}

	@Override
	public double getStrength(ISpiritweb data, boolean getBaseStrength)
	{
		AttributeInstance attribute = data.getLiving().getAttribute(getAttribute());
		if (attribute != null)
		{
			return getBaseStrength ? attribute.getBaseValue() : attribute.getValue();
		}
		return 0;
	}
}
