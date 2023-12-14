/*
 * File updated ~ 10 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;

public class SurgebindingManifestation extends Manifestation
{
	protected final Roshar.Surges surge;

	public SurgebindingManifestation(Roshar.Surges surge)
	{
		super(Manifestations.ManifestationTypes.SURGEBINDING);
		this.surge = surge;
	}

	@Override
	public int getPowerID()
	{
		return surge.getID();
	}

	@Override
	public boolean isActive(ISpiritweb data)
	{
		//surgebinding is different to most powers.

		final SpiritwebCapability spiritwebCapability = (SpiritwebCapability) data;
		SurgebindingSpiritwebSubmodule sb = (SurgebindingSpiritwebSubmodule) spiritwebCapability.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
		return data.hasManifestation(this) && sb.getStormlight() > 0;
	}
}
