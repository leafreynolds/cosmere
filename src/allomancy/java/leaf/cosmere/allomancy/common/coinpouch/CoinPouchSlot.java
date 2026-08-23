/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.coinpouch;

import leaf.cosmere.allomancy.common.items.CoinPouchItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerCopySlot;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

//component backed handlers hand out copies, so ItemHandlerCopySlot writes them back
public class CoinPouchSlot extends ItemHandlerCopySlot
{
	public CoinPouchSlot(IItemHandlerModifiable inv, int index, int x, int y)
	{
		super(new SlotItemHandler(inv, index, x, y)
		{
			@Override
			public boolean mayPlace(@NotNull ItemStack stack)
			{
				return CoinPouchItem.SUPPORTED_PROJECTILES.test(stack);
			}
		});
	}
}
