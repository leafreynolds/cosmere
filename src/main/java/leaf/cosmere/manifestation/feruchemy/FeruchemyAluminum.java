/*
 * File created ~ 27 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.feruchemy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.charge.ItemChargeHelper;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.helpers.EffectsHelper;
import leaf.cosmere.helpers.XPHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class FeruchemyAluminum extends FeruchemyBase
{
    public FeruchemyAluminum(Metals.MetalType metalType)
    {
        super(metalType);
    }

    @Override
    public int modeMin(ISpiritweb data)
    {
        return -1;
    }

    @Override
    public int modeMax(ISpiritweb data)
    {
        return 1;
    }

}
