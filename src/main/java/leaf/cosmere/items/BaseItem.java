/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.world.item.Item;

public class BaseItem extends Item
{

	public BaseItem()
	{
		super(PropTypes.Items.SIXTY_FOUR.get().tab(CosmereItemGroups.ITEMS));
	}

	public BaseItem(Item.Properties prop)
	{
		super(prop);
	}


	protected int getBarWidth(float value, float max)
	{
		return Math.round(13 - ((value * 13)) / max);
	}

}
