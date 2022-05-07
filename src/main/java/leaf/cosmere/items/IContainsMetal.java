/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items;

import net.minecraft.world.item.ItemStack;

public interface IContainsMetal
{
	default boolean containsMetal(ItemStack stack)
	{
		return true;
	}
}
