/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;

public class NecklaceMetalmindItem extends ChargeableMetalCurioItem
{
	public NecklaceMetalmindItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public float getMaxChargeModifier()
	{
		return (6f / 9f);
	}
}
