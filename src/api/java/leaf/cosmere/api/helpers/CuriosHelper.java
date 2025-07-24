package leaf.cosmere.api.helpers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CuriosHelper
{
	public static ICuriosItemHandler getCuriosHandler(LivingEntity entity)
	{
		return CuriosApi.getCuriosInventory(entity).resolve().isPresent()? CuriosApi.getCuriosInventory(entity).resolve().get(): null;
	}

	public static SlotResult getSlot(LivingEntity entity, String slotID, int index) {
		ICuriosItemHandler sub = getCuriosHandler(entity);
		assert sub != null;
		return sub.findCurio(slotID, index).isPresent() ? sub.findCurio(slotID,index).get() : null;
	}

	public static ItemStack getStackInSlot(LivingEntity entity, String slotID, int index)
	{
		if(getSlot(entity, slotID, index) == null)
		{
			return null;
		}
		return Objects.requireNonNull(getSlot(entity, slotID, index)).stack();
	}

	public static SlotContext getContextInSlot(LivingEntity entity, String slotID, int index)
	{
		if(getSlot(entity, slotID, index) == null)
		{
			return null;
		}
		return Objects.requireNonNull(getSlot(entity, slotID, index)).slotContext();
	}

	public static List<SlotResult> getSlotsWithItem(LivingEntity entity, Item item)
	{
		ICuriosItemHandler sub = getCuriosHandler(entity);
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
			if (getSlotsWithItem(entity,item) != null && !getSlotsWithItem(entity,item).isEmpty())
			{
				results.addAll(getSlotsWithItem(entity, item));
			}
		}
		return results;
	}
	public static List<SlotResult> getSlotsWithItem(LivingEntity entity, Collection<? extends Item> items)
	{
		List<SlotResult> results = new ArrayList<>();
		for (Item item: items)
		{
			if (getSlotsWithItem(entity,item) != null && !getSlotsWithItem(entity,item).isEmpty())
			{
				results.addAll(getSlotsWithItem(entity, item));
			}
		}
		return results;
	}

	public static List<SlotResult> getSlotsByIdentifier(LivingEntity entity, String... slotIDs)
	{
		ICuriosItemHandler sub = getCuriosHandler(entity);
		if (sub == null)
		{
			return new ArrayList<SlotResult> ();
		}
		return sub.findCurios(slotIDs);
	}

	public static boolean hasItemInInventory(LivingEntity entity, Item item)
	{
		if(getCuriosHandler(entity) == null)
		{
			return false;
		}
		return Objects.requireNonNull(getCuriosHandler(entity)).isEquipped(item);
	}

	public static boolean hasItemInSlot(LivingEntity entity, String slotID, int index, Item item)
	{
		if(getSlot(entity, slotID, index) == null)
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
