/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;

public class RingMetalmindItem extends ChargeableMetalCurioItem
{
	public RingMetalmindItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public float getMaxChargeModifier()
	{
		return (4f / 9f);
	}
}
