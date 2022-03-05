/*
 * File created ~ 29 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.charge;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.Metalmind;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public class MetalmindChargeHelper
{
    public static boolean adjustMetalmindChargeExact(ISpiritweb data, Metals.MetalType metalType, int chargeToGet, boolean remove, boolean checkPlayer)
    {
        PlayerEntity player = (PlayerEntity) data.getLiving();
        ItemStack metalmind = adjustMetalmindChargeExact(player, metalType, chargeToGet, remove, checkPlayer);
        boolean success = metalmind != null;

        //extra metalmind logic.
        //if we are actually updating the charge inside
        //and if we are adding to it (chargeToGet is negative if adding to metalmind)
        // and if the metal is nicrosil, which is the storing investiture type.
        if (success && remove && chargeToGet < 0 && metalType == Metals.MetalType.NICROSIL)
        {
            //player is storing investiture,
            //set the powers they have to the stack.

            CompoundNBT nbt = metalmind.getOrCreateTagElement("StoredInvestiture");
            //for each power the user has access to
            for (Manifestations.ManifestationTypes manifestationType : Manifestations.ManifestationTypes.values())
            {
                for (int i = 0; i < 16; i++)
                {
                    //even if it's granted from hemalurgy/temporary
                    //update the nbt.
                    //this will add/remove powers based on what the user currently has.
                    //todo, come back to this later when more sleep. bugs me about losing potential stored powers
                    nbt.putBoolean(
                            manifestationType.getManifestation(i).getRegistryName().getPath(),
                            data.hasManifestation(manifestationType, i)
                    );
                }
            }
        }

        return success;
    }

    public static ItemStack adjustMetalmindChargeExact(PlayerEntity player, Metals.MetalType metalType, int chargeToGet, boolean remove, boolean checkPlayer)
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
