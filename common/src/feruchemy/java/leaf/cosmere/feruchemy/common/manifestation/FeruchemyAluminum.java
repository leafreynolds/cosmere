/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;

public class FeruchemyAluminum extends FeruchemyManifestation
{
	public FeruchemyAluminum(Metals.MetalType metalType)
	{
		super(metalType);
	}


	@Override
	public int modeMax(ISpiritweb data)
	{
		return 1;
	}

}
