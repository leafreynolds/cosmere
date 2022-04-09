/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.surgebinding;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class SurgeAdhesion extends SurgebindingBase
{
    public SurgeAdhesion(Roshar.Surges surge)
    {
        super(surge);
    }

    //bind things together
}
