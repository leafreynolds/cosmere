/*
 * File created ~ 29 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.charge;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.MetalmindItem;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

public class MetalmindChargeHelper
{
	public static boolean adjustMetalmindChargeExact(ISpiritweb data, Metals.MetalType metalType, int chargeToGet, boolean remove, boolean checkPlayer)
	{
		Player player = (Player) data.getLiving();
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

			CompoundTag nbt = metalmind.getOrCreateTagElement("StoredInvestiture");
			//for each power the user has access to
			for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
			{
				//even if it's granted from hemalurgy/temporary
				//update the nbt.
				//this will add/remove powers based on what the user currently has.
				//todo, come back to this later when more sleep. bugs me about losing potential stored powers
				if (data.hasManifestation(manifestation))
				{
					nbt.putDouble(manifestation.getName(), manifestation.getStrength(data));
				}
			}
		}

		return success;
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
}
