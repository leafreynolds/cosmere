/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;

public class AllomancyIron extends AllomancyBase
{
    public AllomancyIron(Metals.MetalType metalType)
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

        //Pulls on Nearby Metals
        if (getKeyBinding().isPressed())
        {
            //todo

            //get list of blocks that the user is pulling against

            //add any metal blocks that the user is looking at

            //pull player towards it


            //get list of entities that the user is pulling against

            //add any other valid entities that the user is looking at

            //pull user and entities towards each other
        }
        else
        {
            //release all blocks pulling against

        }
    }
}
