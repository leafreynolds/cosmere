/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;

public class AllomancyNicrosil extends AllomancyBase
{
    public AllomancyNicrosil(Metals.MetalType metalType)
    {
        super(metalType);
    }

    //active or not active
    @Override
    public int modeMax(ISpiritweb data)
    {
        return 1;
    }

    @Override
    public int modeMin(ISpiritweb data)
    {
        return 0;
    }

    @Override
    public boolean modeWraps(ISpiritweb data)
    {
        return false;
    }

    //Enhances Allomantic Burn of Target
    @Override
    protected void performEffect(ISpiritweb data)
    {

        if (getKeyBinding().isPressed())
        {
            //todo

        }


    }


}
