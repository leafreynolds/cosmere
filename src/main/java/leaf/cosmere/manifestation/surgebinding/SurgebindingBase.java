/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.surgebinding;

import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.manifestation.ManifestationBase;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.RegistryObject;

public class SurgebindingBase extends ManifestationBase
{
	protected final Roshar.Surges surge;
	public SurgebindingBase(Roshar.Surges surge)
	{
		super(Manifestations.ManifestationTypes.SURGEBINDING, surge.getColor());
		this.surge = surge;
	}

	@Override
	public RegistryObject<Attribute> getAttribute()
	{
		return surge.getAttribute();
	}
}
