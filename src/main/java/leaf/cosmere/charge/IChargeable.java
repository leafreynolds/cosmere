/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to Vazkii and their Mod Botania for providing the itemstack NBT interaction example!
 * https://github.com/Vazkii/Botania
 * I've used their example of storing mana as the basis for storing different types of feruchemical attributes.
 * In future, will also be doing it for gems and stormlight.
 */

package leaf.cosmere.charge;

import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.IHasMetalType;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.utils.helpers.PlayerHelper;
import leaf.cosmere.utils.helpers.StackNBTHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

//item based chargeable
public interface IChargeable
{
	default int getMaxCharge(ItemStack itemStack)
	{
		//todo config max value
		return Mth.floor(18000 * getMaxChargeModifier());
	}

	default float getMaxChargeModifier()
	{
		return 1;
	}

	default int getCharge(ItemStack itemStack)
	{
		return StackNBTHelper.getInt(itemStack, Constants.NBT.CHARGE_LEVEL, 0);
	}

	default void setCharge(ItemStack itemStack, int chargeLevel)
	{
		StackNBTHelper.setInt(itemStack, Constants.NBT.CHARGE_LEVEL, Mth.clamp(chargeLevel, 0, this.getMaxCharge(itemStack)));

		if (chargeLevel == 0 && getAttunedPlayer(itemStack) != null)
		{
			StackNBTHelper.removeEntry(itemStack, Constants.NBT.ATTUNED_PLAYER);
			StackNBTHelper.removeEntry(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME);
		}
	}

	default boolean trySetAttunedPlayer(ItemStack itemStack, Player entity)
	{
		if (itemStack.getItem() instanceof IHasMetalType metalType)
		{
			UUID attunedPlayerID = getAttunedPlayer(itemStack);
			UUID playerID = entity.getUUID();
			boolean noAttunedPlayer = attunedPlayerID == null;

			//only allow unkeyed metalminds if they aren't aluminum
			if (noAttunedPlayer && metalType.getMetalType() != Metals.MetalType.ALUMINUM)
			{
				//No attuned player! Check to see whether they are storing identity
				MobEffectInstance storingIdentity = entity.getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.ALUMINUM).get());
				//if they are
				if (storingIdentity != null && storingIdentity.getDuration() > 0)
				{
					//then set the metalmind to "unsealed". Any feruchemist with access to that power can use the metalmind
					StackNBTHelper.setUuid(itemStack, Constants.NBT.ATTUNED_PLAYER, Constants.NBT.UNKEYED_UUID);
					StackNBTHelper.setString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, "Unkeyed"); // todo translation
					return true;

				}
			}

			//if theres no attuned player on the metalmind
			//or if the player is attuned to the metalmind
			//or if the metalmind is unsealed (anyone can access)
			if (noAttunedPlayer || attunedPlayerID.compareTo(playerID) == 0 || attunedPlayerID.compareTo(Constants.NBT.UNKEYED_UUID) == 0)
			{
				if (noAttunedPlayer && getCharge(itemStack) > 0)
				{
					setAttunedPlayer(itemStack, entity);
					setAttunedPlayerName(itemStack, entity);
				}
				//auto success if that player is already attuned
				return true;
			}

		}
		return false;
	}

	default void setAttunedPlayer(ItemStack itemStack, Player entity)
	{
		StackNBTHelper.setUuid(itemStack, Constants.NBT.ATTUNED_PLAYER, entity.getUUID());
	}

	default UUID getAttunedPlayer(ItemStack itemStack)
	{
		return StackNBTHelper.getUuid(itemStack, Constants.NBT.ATTUNED_PLAYER);
	}

	default void setAttunedPlayerName(ItemStack itemStack, Player entity)
	{
		String playerName = entity.getDisplayName().getString();
		StackNBTHelper.setString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, playerName);
	}

	default String getAttunedPlayerName(ItemStack itemStack)
	{
		return StackNBTHelper.getString(itemStack, Constants.NBT.ATTUNED_PLAYER_NAME, "");
	}

	default boolean getPlayerIsAttuned(ItemStack itemStack, Player entity)
	{
		MobEffectInstance storingIdentityEffect = entity.getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.ALUMINUM).get());
		boolean noIdentityPlayer = storingIdentityEffect != null && storingIdentityEffect.getDuration() > 0;

		UUID itemAttunedPlayerUUID = getAttunedPlayer(itemStack);
		//null means not attuned at all, so can assume player is attuned with it
		return noIdentityPlayer || itemAttunedPlayerUUID == null || itemAttunedPlayerUUID == entity.getUUID();
	}


	default void adjustCharge(ItemStack stack, int chargeToAdjustBy)
	{
		int currentCharge = getCharge(stack);
		int goalCharge = currentCharge + chargeToAdjustBy;
		int actualGoalCharge = Math.min(goalCharge, getMaxCharge(stack));
		int finalChargeValue = actualGoalCharge / stack.getCount();

		setCharge(stack, finalChargeValue);
	}

	default void increaseCurrentCharge(ItemStack itemStack)
	{
		increaseCurrentCharge(itemStack, 1);
	}

	default void increaseCurrentCharge(ItemStack stack, int i)
	{
		setCharge(stack, getCharge(stack) + i);
	}

	default void decreaseCurrentCharge(ItemStack itemStack)
	{
		int currentCharge = getCharge(itemStack);
		int nextChargeLevel = currentCharge - 1;
		setCharge(itemStack, nextChargeLevel);
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
