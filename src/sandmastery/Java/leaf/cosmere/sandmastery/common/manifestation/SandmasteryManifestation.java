/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class SandmasteryManifestation extends Manifestation
{
	protected final Taldain.Investiture investiture;

	public SandmasteryManifestation(Taldain.Investiture investiture)
	{
		super(Manifestations.ManifestationTypes.SANDMASTERY);
		this.investiture = investiture;
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
