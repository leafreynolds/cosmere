/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.coinpouch;

import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CoinPouchInventory
{
	private final ItemStackHandler inv = new ItemStackHandler(18)
	{
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			return !stack.isEmpty() && CoinPouchItem.SUPPORTED_PROJECTILES.test(stack);
		}
	};

	public IItemHandlerModifiable getHandler()
	{
		return inv;
	}
}
