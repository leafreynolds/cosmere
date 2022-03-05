/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.curios;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

//Adding extra curio functionality, BUT ONLY if curio mod is installed.
//'ideally' you would extend ICurioItem on the key item itself, but that makes the Curio mod required to be installed, which isn't guaranteed.
public class TestCurio implements ICurio
{

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity)
    {
        //don't check every tick.
        if (livingEntity.tickCount % 20 == 0)
        {

        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack)
    {

    }
}
