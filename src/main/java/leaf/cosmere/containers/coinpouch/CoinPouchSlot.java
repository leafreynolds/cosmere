/*
 * File created ~ 21 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.containers.coinpouch;

import leaf.cosmere.items.CoinPouchItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CoinPouchSlot extends SlotItemHandler
{
	public CoinPouchSlot(IItemHandlerModifiable inv, int index, int x, int y)
	{
		super((IItemHandler) inv, index, x, y);
	}

	@Override
	public boolean mayPlace(@NotNull ItemStack stack)
	{
		final boolean test = CoinPouchItem.SUPPORTED_PROJECTILES.test(stack);
		return test;
	}

	@Override
	public void setChanged()
	{
		container.setChanged();
	}
}
