/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.*;
import leaf.cosmere.helpers.*;
import leaf.cosmere.registry.*;
import net.minecraft.entity.*;
import net.minecraft.potion.*;
import net.minecraft.util.math.MathHelper;

import java.util.*;

import static leaf.cosmere.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyDuralumin extends AllomancyBase
{
	public AllomancyDuralumin(Metals.MetalType metalType)
    {
		super(metalType);
	}


	//Enhances Current Metals Burned
	@Override
	protected void performEffect(ISpiritweb data)
    {
        LivingEntity livingEntity = data.getLiving();
        boolean isActiveTick = livingEntity.ticksExisted % 20 == 0;

        if (isActiveTick)
		{
			//drain metals that are actively being burned
			for (Metals.MetalType metalType : Metals.MetalType.values())
			{
			    if (!metalType.hasAssociatedManifestation())
			        continue;

				//if metal is active
				if (data.manifestationActive(Manifestations.ManifestationTypes.ALLOMANCY, metalType.getID()))
				{

                    int ingestedMetalAmount = data.getIngestedMetal(metalType);

                    if (ingestedMetalAmount > 0)
                    {
                        if (ingestedMetalAmount >= 27)
                        {
                            data.adjustIngestedMetal(metalType, ingestedMetalAmount - 27, true);
                        }
                        else
                        {
                            data.adjustIngestedMetal(metalType, ingestedMetalAmount, true);
                        }
                    }
				}
			}

            //apply the effect regardless, because duralumin is currently active.
            EffectInstance newEffect = EffectsHelper.getNewEffect(
                    EffectsRegistry.ALLOMANCY_BOOST.get(),
                    MathHelper.floor(getAllomanticStrength(data))
            );

            data.getLiving().addPotionEffect(newEffect);

		}

	}


}
