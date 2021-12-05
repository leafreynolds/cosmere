/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.items.Metalmind;

public class RingMetalmind extends Metalmind
{
    public RingMetalmind(Metals.MetalType metalType)
    {
        super(metalType, CosmereItemGroups.METALMINDS);
    }


    @Override
    public float getMaxChargeModifier()
    {
        return (3f / 9f);
    }

}
