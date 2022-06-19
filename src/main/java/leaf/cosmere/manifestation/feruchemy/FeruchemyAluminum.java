/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;

public class FeruchemyAluminum extends FeruchemyBase
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
