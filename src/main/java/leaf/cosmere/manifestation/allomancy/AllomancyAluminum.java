/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;

public class AllomancyAluminum extends AllomancyBase
{
    public AllomancyAluminum(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    protected void performEffect(ISpiritweb data)
    {
        //passive active ability, if any
        {
            //drain all metals
            for (Metals.MetalType metalType : Metals.MetalType.values())
            {
                int ingestedMetalAmount = data.getIngestedMetal(metalType);
                if (ingestedMetalAmount > 5)
                {
                    data.adjustIngestedMetal(metalType, ingestedMetalAmount / 2, true);
                }
                else
                {
                    data.adjustIngestedMetal(metalType, ingestedMetalAmount, true);
                }
            }

        }
    }


}
