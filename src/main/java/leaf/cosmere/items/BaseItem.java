/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.properties.PropTypes;
import net.minecraft.item.Item;

public class BaseItem extends Item
{

    public BaseItem()
    {
        super(PropTypes.Items.SIXTY_FOUR.get().group(CosmereItemGroups.ITEMS));
    }

    public BaseItem(Item.Properties prop)
    {
        super(prop);
    }


}
