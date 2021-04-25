/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to Vazkii and their Mod Botania for providing the itemstack NBT interaction example!
 * https://github.com/Vazkii/Botania
 * I've used their example of storing mana as the basis for storing different types of feruchemical attributes.
 * In future, will also be doing it for gems and stormlight.
 */

package leaf.cosmere.charge;

import leaf.cosmere.helpers.NBTHelper;
import leaf.cosmere.helpers.PlayerHelper;
import leaf.cosmere.constants.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

//item based chargeable
public interface IChargeable
{
    default int getMaxCharge(ItemStack itemStack)
    {
        return MathHelper.floor(18000 * getMaxChargeModifier());
    }

    default float getMaxChargeModifier()
    {
        return 1;
    }

    default int getCharge(ItemStack itemStack)
    {
        return NBTHelper.getInt(itemStack, Constants.NBT.CHARGE_LEVEL, 0);
    }

    default void setCharge(ItemStack itemStack, int chargeLevel)
    {
        NBTHelper.setInt(itemStack, Constants.NBT.CHARGE_LEVEL, MathHelper.clamp(chargeLevel, 0, this.getMaxCharge(itemStack)));
    }

    default boolean trySetAttunedPlayer(ItemStack itemStack, PlayerEntity entity)
    {
        UUID attunedPlayerID = getAttunedPlayer(itemStack);

        boolean noAttunedPlayer = attunedPlayerID == null;

        UUID playerID = entity.getUniqueID();
        if (noAttunedPlayer || attunedPlayerID.compareTo(playerID) == 0)
        {
            if (noAttunedPlayer)
            {
                setAttunedPlayer(itemStack, entity);
                setAttunedPlayerName(itemStack, entity);
            }
            //auto success if that player is already attuned
            return true;
        }

        return false;
    }

    default void setAttunedPlayer(ItemStack itemStack, PlayerEntity entity)
    {
        NBTHelper.setUuid(itemStack, Constants.NBT.ATTUNED_PLAYER, entity.getUniqueID());
    }

    default UUID getAttunedPlayer(ItemStack itemStack)
    {
        return NBTHelper.getUuid(itemStack, Constants.NBT.ATTUNED_PLAYER);
    }

    default void setAttunedPlayerName(ItemStack itemStack, PlayerEntity entity)
    {
        String playerName = PlayerHelper.getPlayerName(entity.getUniqueID(), entity.getServer());
        NBTHelper.setString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, playerName);
    }

    default String getAttunedPlayerName(ItemStack itemStack)
    {
        return NBTHelper.getString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, "");
    }

    default boolean getPlayerIsAttuned(ItemStack itemStack, PlayerEntity entity)
    {
        UUID uuid = getAttunedPlayer(itemStack);
        //null means not attuned at all, so can assume player is attuned with it
        return uuid == null || uuid == entity.getUniqueID();
    }


    default void adjustCharge(ItemStack stack, int chargeToAdjustBy)
    {
        int currentCharge = getCharge(stack);
        int goalCharge = currentCharge + chargeToAdjustBy;
        int actualGoalCharge = Math.min(goalCharge, getMaxCharge(stack));
        int finalChargeValue = (int) (actualGoalCharge / stack.getCount());

        setCharge(stack, finalChargeValue);
    }

    default void increaseCurrentCharge(ItemStack itemStack)
    {
        int currentCharge = getCharge(itemStack);
        int nextChargeLevel = currentCharge + 1;
        setCharge(itemStack, (int) nextChargeLevel);
    }

    default void decreaseCurrentCharge(ItemStack itemStack)
    {
        int currentCharge = getCharge(itemStack);
        int nextChargeLevel = currentCharge - 1;
        setCharge(itemStack, (int) nextChargeLevel);
    }

    default boolean canGiveChargeToItem(ItemStack stackInSlot, ItemStack stack)
    {
        return true;
    }

    default boolean canReceiveChargeFromItem(ItemStack stack, ItemStack stackInSlot)
    {
        return true;
    }
}
