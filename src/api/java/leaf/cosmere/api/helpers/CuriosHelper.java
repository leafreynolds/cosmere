package leaf.cosmere.api.helpers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.*;

public class CuriosHelper
{
	public static Optional<ICuriosItemHandler> getCuriosHandler(LivingEntity entity)
	{
		return CuriosApi.getCuriosInventory(entity).resolve();
	}

	public static Optional<SlotResult> getSlot(LivingEntity entity, String slotID, int index) {
		ICuriosItemHandler sub = getCuriosHandler(entity).orElse(null);
		if(sub == null)
		{
			return Optional.empty();
		}
		return sub.findCurio(slotID, index);
	}

	public static ItemStack getStackInSlot(LivingEntity entity, String slotID, int index)
	{
		var slot = getSlot(entity, slotID, index).orElse(null);
		if(slot == null)
		{
			return ItemStack.EMPTY;
		}
		return Objects.requireNonNull(slot).stack();
	}

	public static Optional<SlotContext> getContextInSlot(LivingEntity entity, String slotID, int index)
	{
		var slot = getSlot(entity, slotID, index).orElse(null);
		if(slot == null)
		{
			return Optional.empty();
		}
		return Optional.of(slot.slotContext());
	}

	public static List<SlotResult> getSlotsWithItem(LivingEntity entity, Item item)
	{
		ICuriosItemHandler sub = getCuriosHandler(entity).orElse(null);
		if (sub == null)
		{
			return new ArrayList<SlotResult> ();
		}
		return sub.findCurios(item);
	}
	public static List<SlotResult> getSlotsWithItem(LivingEntity entity, Item... items)
	{
		List<SlotResult> results = new ArrayList<>();
		for (Item item: items)
		{
			var slotsWithItem = getSlotsWithItem(entity, item);
			if (slotsWithItem != null && !slotsWithItem.isEmpty())
			{
				results.addAll(slotsWithItem);
			}
		}
		return results;
	}
	public static List<SlotResult> getSlotsWithItem(LivingEntity entity, Collection<? extends Item> items)
	{
		List<SlotResult> results = new ArrayList<>();
		for (Item item: items)
		{
			var slotsWithItem = getSlotsWithItem(entity, item);
			if (slotsWithItem != null && !slotsWithItem.isEmpty())
			{
				results.addAll(slotsWithItem);
			}
		}
		return results;
	}

	public static List<SlotResult> getSlotsByIdentifier(LivingEntity entity, String... slotIDs)
	{
		ICuriosItemHandler sub = getCuriosHandler(entity).orElse(null);
		if (sub == null)
		{
			return new ArrayList<SlotResult> ();
		}
		return sub.findCurios(slotIDs);
	}

	public static boolean hasItemInInventory(LivingEntity entity, Item item)
	{
		var handler = getCuriosHandler(entity).orElse(null);
		if(handler == null)
		{
			return false;
		}
		return Objects.requireNonNull(handler).isEquipped(item);
	}

	public static boolean hasItemInSlot(LivingEntity entity, String slotID, int index, Item item)
	{
		var slot = getSlot(entity, slotID, index).orElse(null);
		if(slot == null)
		{
			return false;
		}
		return getStackInSlot(entity, slotID, index).getItem().equals(item);
	}



	public static boolean hasItemInSlot(LivingEntity entity, String slotID, int index, Item... items)
	{
		for (Item item: items)
		{
			if(hasItemInSlot(entity, slotID, index, item))
			{
				return true;
			}
		}
		return false;
	}
	public static boolean hasItemInSlot(LivingEntity entity, String slotID, int index, Collection<? extends Item> items)
	{
		for (Item item: items)
		{
			if(hasItemInSlot(entity, slotID, index, item))
			{
				return true;
			}
		}
		return false;
	}

}
