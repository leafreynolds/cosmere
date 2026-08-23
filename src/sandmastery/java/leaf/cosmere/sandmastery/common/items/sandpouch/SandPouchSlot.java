/*
 * File updated ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerCopySlot;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SandPouchSlot extends ItemHandlerCopySlot
{
	public SandPouchSlot(IItemHandlerModifiable inv, int index, int x, int y, boolean input)
	{
		super(new SlotItemHandler(inv, index, x, y)
		{
			@Override
			public boolean mayPlace(@NotNull ItemStack stack)
			{
				return input && SandPouchItem.SUPPORTED_ITEMS.test(stack);
			}
		});
	}
}
