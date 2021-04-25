/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;

public class AllomancyDuralumin extends AllomancyBase
{
    public AllomancyDuralumin(Metals.MetalType metalType)
    {
        super(metalType);
    }


    //Enhances Current Metal Burned
    @Override
    protected void performEffect(ISpiritweb data)
    {
        //todo
        if (getKeyBinding().isPressed())
        {

        }


    }


}
