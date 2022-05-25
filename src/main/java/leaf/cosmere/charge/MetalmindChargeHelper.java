/*
 * File created ~ 29 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.charge;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.MetalmindItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class MetalmindChargeHelper
{
	public static ItemStack adjustMetalmindChargeExact(ISpiritweb data, Metals.MetalType metalType, int chargeToGet, boolean remove, boolean checkPlayer)
	{
		if (data.getLiving() instanceof Player player)
		{
			return adjustMetalmindChargeExact(player, metalType, chargeToGet, remove, checkPlayer);
		}
		else
		{
			return null;
		}
	}

	private static Predicate<ItemStack> getIsItemInvalidMetalmind(Metals.MetalType metalType)
	{
		return obj ->
		{
			if (obj.getItem() instanceof final MetalmindItem item)
			{

				//Correct metal or harmonium which I'm using as universal
				return item.getMetalType() != metalType && item.getMetalType() != Metals.MetalType.HARMONIUM;
			}
			return false;
		};
	}

	public static ItemStack adjustMetalmindChargeExact(Player player, Metals.MetalType metalType, int chargeToGet, boolean remove, boolean checkPlayer)
	{
		List<ItemStack> items = ItemChargeHelper.getChargeItems(player);
		List<ItemStack> acc = ItemChargeHelper.getChargeCurios(player);

		//remove items that don't match the metal type we are looking for
		items.removeIf(getIsItemInvalidMetalmind(metalType));
		acc.removeIf(getIsItemInvalidMetalmind(metalType));

		if (items.isEmpty() && acc.isEmpty())
		{
			return null;
		}

		return ItemChargeHelper.adjustChargeExact(player, chargeToGet, remove, checkPlayer, items, acc);
	}
}
