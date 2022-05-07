/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.world.item.Rarity;

public class GemItem extends ChargeableItemBase
{
	public GemItem()
	{
		super(PropTypes.Items.SIXTY_FOUR.get().tab(CosmereItemGroups.ITEMS).rarity(Rarity.UNCOMMON));
	}

}
