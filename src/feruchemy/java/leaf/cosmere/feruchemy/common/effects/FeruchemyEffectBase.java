/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.effects;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;

public class FeruchemyEffectBase extends CosmereEffect implements IHasMetalType
{
	protected final Metals.MetalType metalType;

	public FeruchemyEffectBase(Metals.MetalType type)
	{
		super();
		metalType = type;
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}

	@Override
	protected int getTickOffset()
	{
		return metalType.getID();
	}

}
