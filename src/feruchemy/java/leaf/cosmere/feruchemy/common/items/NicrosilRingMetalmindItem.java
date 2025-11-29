/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.PowerMetalCurioItem;

public class NicrosilRingMetalmindItem extends PowerMetalCurioItem
{
	public NicrosilRingMetalmindItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int getMaxCapacity()
	{
		return 1;
	}

}
