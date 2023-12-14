/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 * Largely copied from the Botania Mod!
 * this is used for detecting items and curios that have charge interface attached
 */

package leaf.cosmere.common.items;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class CapWrapper implements Container
{
	private final IItemHandler handler;

	public CapWrapper(IItemHandler handler)
	{
		this.handler = handler;
	}

	@Override
	public int getContainerSize()
	{
		return handler.getSlots();
	}

	@Override
	public boolean isEmpty()
	{
		for (int i = 0; i < getContainerSize(); i++)
		{
			if (!getItem(i).isEmpty())
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int index)
	{
		return handler.getStackInSlot(index);
	}

	@Override
	public ItemStack removeItem(int index, int count)
	{
		return handler.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index)
	{
		return handler.extractItem(index, Integer.MAX_VALUE, false);
	}

	@Override
	public void setItem(int index, ItemStack stack)
	{
		if (handler instanceof IItemHandlerModifiable)
		{
			((IItemHandlerModifiable) handler).setStackInSlot(index, stack);
		}
	}

	@Override
	public void setChanged()
	{

	}

	@Override
	public boolean stillValid(Player player)
	{
		return false;
	}

	@Override
	public void clearContent()
	{
		for (int i = 0; i < getContainerSize(); i++)
		{
			removeItemNoUpdate(i);
		}
	}
}
