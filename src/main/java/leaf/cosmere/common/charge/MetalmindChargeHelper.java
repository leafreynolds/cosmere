/*
 * File updated ~ 24 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.common.charge;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class MetalmindChargeHelper
{
	public static ItemStack adjustMetalmindChargeExact(ISpiritweb data, Metals.MetalType metalType, int adjustAmount, boolean remove, boolean checkPlayer)
	{
		if (data.getLiving() instanceof Player player)
		{
			return adjustMetalmindChargeExact(player, metalType, adjustAmount, remove, checkPlayer);
		}
		else
		{
			//todo ??
			return ItemStack.EMPTY;
		}
	}

	private static Predicate<ItemStack> getIsItemInvalidMetalmind(Metals.MetalType metalType)
	{
		return obj ->
		{
			if (obj.getItem() instanceof final ChargeableMetalCurioItem item)
			{
				//Correct metal or harmonium which I'm using as universal
				final Metals.MetalType itemMetalType = item.getMetalType();
				return itemMetalType != metalType && itemMetalType != Metals.MetalType.HARMONIUM;
			}
			return false;
		};
	}

	public static ItemStack adjustMetalmindChargeExact(Player player, Metals.MetalType metalType, int adjustValue, boolean remove, boolean checkPlayer)
	{
		List<ItemStack> ordered = buildOrderedMetalminds(player, metalType);

		if (ordered.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		return ItemChargeHelper.adjustChargeExact(player, adjustValue, remove, checkPlayer, ordered, Collections.emptyList());
	}

	/**
	 * Checks whether the full cost can be met across all accessible metalminds, then (if doAdjust)
	 * drains/stores from them in priority order. Never partially applies: returns false without
	 * touching any metalmind if the total available charge is less than the required amount.
	 *
	 * @return true only if the full cost could be (and was, when doAdjust=true) satisfied
	 */
	public static boolean adjustMetalmindCharges(ISpiritweb data, Metals.MetalType metalType, int adjustValue, boolean doAdjust, boolean checkPlayer)
	{
		if (!(data.getLiving() instanceof Player player))
		{
			return false;
		}

		boolean isStoringIdentity = false;
		{
			Optional<ISpiritweb> spiritwebData = SpiritwebCapability.get(player);
			if (spiritwebData.isPresent())
			{
				isStoringIdentity = Manifestations.ManifestationTypes.FERUCHEMY.getManifestation(Metals.MetalType.ALUMINUM.getID()).getMode(spiritwebData.get()) > 0;
			}
		}

		boolean storing = adjustValue > 0;
		int needed = Math.abs(adjustValue);

		// Pass 1: collect accessible metalminds and verify the full cost can be met.
		List<ItemStack> accessible = new ArrayList<>();
		int totalAvailable = 0;

		for (ItemStack stackInSlot : buildOrderedMetalminds(player, metalType))
		{
			IChargeable chargeItemSlot = (IChargeable) stackInSlot.getItem();
			int slotCharge = chargeItemSlot.getCharge(stackInSlot);
			int slotMaxCharge = chargeItemSlot.getMaxCharge(stackInSlot);

			int available = storing ? (slotMaxCharge - slotCharge) : slotCharge;
			if (available <= 0)
			{
				continue;
			}

			// Read-only access check — no NBT writes in the check pass.
			UUID attunedPlayer = chargeItemSlot.getAttunedPlayer(stackInSlot);
			if (checkPlayer && attunedPlayer != null
					&& attunedPlayer.compareTo(player.getUUID()) != 0
					&& attunedPlayer.compareTo(Constants.NBT.UNKEYED_UUID) != 0)
			{
				continue; // attuned to a different player
			}
			if (storing && !isStoringIdentity && attunedPlayer != null && attunedPlayer.compareTo(Constants.NBT.UNKEYED_UUID) == 0)
			{
				continue; // can't store in an unsealed metalmind without storing identity
			}

			accessible.add(stackInSlot);
			totalAvailable += available;

			if (totalAvailable >= needed)
			{
				break; // no need to keep scanning once we know it's affordable
			}
		}

		if (totalAvailable < needed)
		{
			return false;
		}

		// Pass 2: apply the charge adjustment across the cached metalminds.
		// trySetAttunedPlayer is called here so attunement side effects only happen when charge actually moves.
		if (doAdjust)
		{
			int remaining = needed;
			for (ItemStack stackInSlot : accessible)
			{
				if (remaining <= 0)
				{
					break;
				}
				IChargeable chargeItemSlot = (IChargeable) stackInSlot.getItem();
				int slotCharge = chargeItemSlot.getCharge(stackInSlot);
				int slotMaxCharge = chargeItemSlot.getMaxCharge(stackInSlot);
				int amount = storing
						? Math.min(remaining, slotMaxCharge - slotCharge)
						: Math.min(remaining, slotCharge);
				chargeItemSlot.trySetAttunedPlayer(stackInSlot, player);
				chargeItemSlot.adjustCharge(stackInSlot, storing ? amount : -amount);
				remaining -= amount;
			}
		}

		return true;
	}

	/** Builds an ordered list of metalminds: hotbar → curios → armor → main inventory. */
	private static List<ItemStack> buildOrderedMetalminds(Player player, Metals.MetalType metalType)
	{
		Predicate<ItemStack> isInvalid = getIsItemInvalidMetalmind(metalType);
		Predicate<ItemStack> isValid = stack ->
				!stack.isEmpty()
				&& stack.getItem() instanceof IChargeable
				&& !isInvalid.test(stack);

		Inventory inv = player.getInventory();
		List<ItemStack> result = new ArrayList<>();

		for (int i = 0; i < 9; i++)
		{
			ItemStack s = inv.items.get(i);
			if (isValid.test(s)) result.add(s);
		}

		for (ItemStack s : ItemChargeHelper.getChargeCurios(player))
		{
			if (isValid.test(s)) result.add(s);
		}

		for (ItemStack s : inv.armor)
		{
			if (isValid.test(s)) result.add(s);
		}

		for (int i = 9; i < inv.items.size(); i++)
		{
			ItemStack s = inv.items.get(i);
			if (isValid.test(s)) result.add(s);
		}

		return result;
	}
}
