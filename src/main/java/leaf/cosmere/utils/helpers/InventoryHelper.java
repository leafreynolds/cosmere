/*
 * File created ~ 10 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class InventoryHelper
{

	public static void swapStack(Inventory inv, int stack1, int stack2)
	{
		ItemStack tempStack = inv.getItem(stack2);
		inv.setItem(stack2, inv.getItem(stack1));
		inv.setItem(stack1, tempStack);
	}
}
