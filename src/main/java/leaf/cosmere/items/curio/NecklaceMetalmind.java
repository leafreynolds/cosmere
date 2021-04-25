/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.Metalmind;

public class NecklaceMetalmind extends Metalmind
{
    public NecklaceMetalmind(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    public float getMaxChargeModifier()
    {
        return (4f / 9f);
    }
}
