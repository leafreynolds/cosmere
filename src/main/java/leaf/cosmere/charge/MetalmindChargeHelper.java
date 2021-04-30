/*
 * File created ~ 29 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.charge;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.Metalmind;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MetalmindChargeHelper
{



    public static boolean adjustMetalmindChargeExact(PlayerEntity player, Metals.MetalType metalType, int chargeToGet, boolean remove, boolean checkPlayer)
    {
        List<ItemStack> items = ItemChargeHelper.getChargeItems(player);
        List<ItemStack> acc = ItemChargeHelper.getChargeCurios(player);

        //remove items that don't match the metal type we are looking for
        items.removeIf(obj ->
                {
                    boolean objectIsNotMetalmind = !(obj.getItem() instanceof Metalmind);
                    boolean metalMindIsNotCorrectType = ((Metalmind) obj.getItem()).getMetalType() != metalType;

                    return (objectIsNotMetalmind || metalMindIsNotCorrectType);
                }
        );
        acc.removeIf(obj ->
                {
                    boolean objectIsNotMetalmind = !(obj.getItem() instanceof Metalmind);
                    boolean metalMindIsNotCorrectType = ((Metalmind) obj.getItem()).getMetalType() != metalType;

                    return (objectIsNotMetalmind || metalMindIsNotCorrectType);
                }
        );

        return ItemChargeHelper.adjustChargeExact(player, chargeToGet, remove, checkPlayer, items, acc);
    }
}
