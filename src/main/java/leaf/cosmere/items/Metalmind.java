/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.constants.Metals;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class Metalmind extends ChargeableMetalItem implements ICurioItem
{
    public Metalmind(Metals.MetalType metalType)
    {
        super(metalType);
    }
}
