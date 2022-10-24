/*
 * File updated ~ 24 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.common.charge;

import leaf.cosmere.api.Metals;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
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
		List<ItemStack> items = ItemChargeHelper.getChargeItems(player);
		List<ItemStack> acc = ItemChargeHelper.getChargeCurios(player);

		//remove items that don't match the metal type we are looking for
		items.removeIf(getIsItemInvalidMetalmind(metalType));
		acc.removeIf(getIsItemInvalidMetalmind(metalType));

		if (items.isEmpty() && acc.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		return ItemChargeHelper.adjustChargeExact(player, adjustValue, remove, checkPlayer, items, acc);
	}
}
