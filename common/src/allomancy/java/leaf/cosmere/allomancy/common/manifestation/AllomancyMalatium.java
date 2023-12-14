/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.manifestation;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;

public class AllomancyMalatium extends AllomancyManifestation
{
	public AllomancyMalatium(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void applyEffectTick(ISpiritweb data)
	{
		//Reveals other's past
		{
			//todo
		}
	}
	//add client side stuff

}
