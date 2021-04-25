/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;

public class AllomancySteel extends AllomancyBase
{
    public AllomancySteel(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        //passive active ability, if any
        {
            //see metal. Probably wait for the render tick?
            //todo
        }

        //Pushes on Nearby Metals
        if (getKeyBinding().isPressed())
        {


            //get list of blocks that the user is pushing against

            //add any metal blocks that the user is looking at

            //push user away from it


            //get list of entities that the user is pushing against

            //add any other valid entities that the user is looking at

            //push user and entities towards each other
        }


    }


}
