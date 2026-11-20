/*
 * File updated ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.items.sandpouch;

import leaf.cosmere.sandmastery.common.items.SandPouchItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SandPouchSlot extends SlotItemHandler
{
	private final boolean input;
	private final boolean clientSide;

	public SandPouchSlot(IItemHandlerModifiable inv, int index, int x, int y, boolean input, boolean clientSide)
	{
		super(inv, index, x, y);
		this.input = input;
		this.clientSide = clientSide;
	}

	// The server's slot sync lands here on the client. The slot only displays the pouch, which the client
	// already reads live, so applying the synced stack as a "take" or "insert" would change the pouch twice.
	@Override
	public void set(@NotNull ItemStack stack)
	{
		if (!clientSide)
		{
			super.set(stack);
		}
	}

	@Override
	public boolean mayPlace(@NotNull ItemStack stack)
	{
		return input && SandPouchItem.SUPPORTED_ITEMS.test(stack);
	}
}
