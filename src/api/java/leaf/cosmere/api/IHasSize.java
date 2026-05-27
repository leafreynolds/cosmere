package leaf.cosmere.api;

import leaf.cosmere.api.helpers.StackNBTHelper;
import net.minecraft.world.item.ItemStack;

public interface IHasSize
{
	int getMaxSize();

	int getMinSize();

	default Integer readMetalAlloySizeNbtData(ItemStack itemStack)
	{
		if (StackNBTHelper.verifyExistance(itemStack, "nuggetSize"))
		{
			return StackNBTHelper.getInt(itemStack, "nuggetSize", getMaxSize());
		}
		writeMetalAlloySizeNbtData(itemStack, getMaxSize());
		return getMaxSize();
	}

	default boolean writeMetalAlloySizeNbtData(ItemStack itemStack, int size)
	{
		if (size > getMaxSize() || size < getMinSize()) return false;
		StackNBTHelper.setInt(itemStack, "nuggetSize", size);
		return true;
	}
}
