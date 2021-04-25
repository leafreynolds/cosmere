/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.Metalmind;

public class BraceletMetalmind extends Metalmind
{
    public BraceletMetalmind(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    public float getMaxChargeModifier()
    {
        return (6f / 9f);
    }
}
