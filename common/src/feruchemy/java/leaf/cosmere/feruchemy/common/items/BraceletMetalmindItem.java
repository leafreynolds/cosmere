/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import leaf.cosmere.feruchemy.common.itemgroups.FeruchemyItemGroups;

public class BraceletMetalmindItem extends ChargeableMetalCurioItem
{
	public BraceletMetalmindItem(Metals.MetalType metalType)
	{
		super(metalType, FeruchemyItemGroups.METALMINDS);
	}

	@Override
	public float getMaxChargeModifier()
	{
		return (5f / 9f);
	}

}
