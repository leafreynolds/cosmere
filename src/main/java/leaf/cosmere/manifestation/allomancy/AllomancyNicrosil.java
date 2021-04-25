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
