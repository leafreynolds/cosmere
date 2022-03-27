/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.items.MetalmindItem;

public class RingMetalmindItem extends MetalmindItem
{
    public RingMetalmindItem(Metals.MetalType metalType)
    {
        super(metalType, CosmereItemGroups.METALMINDS);
    }

    @Override
    public float getMaxChargeModifier()
    {
        return (4f / 9f);
    }
}
