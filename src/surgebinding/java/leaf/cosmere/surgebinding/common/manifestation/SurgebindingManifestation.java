/*
 * File updated ~ 23 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.manifestation.Manifestation;

public class SurgebindingManifestation extends Manifestation
{
	protected final Roshar.Surges surge;

	public SurgebindingManifestation(Roshar.Surges surge)
	{
		super(Manifestations.ManifestationTypes.SURGEBINDING);
		this.surge = surge;
	}
}
