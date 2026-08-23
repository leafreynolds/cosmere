/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.items;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.PowerMetalCurioItem;

public class NicrosilBraceletMetalmindItem extends PowerMetalCurioItem
{
	public NicrosilBraceletMetalmindItem(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	public int getMaxCapacity()
	{
		return 2;
	}

}
