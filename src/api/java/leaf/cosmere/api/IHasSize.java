package leaf.cosmere.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IHasSize
{
	int getMaxSize();

	int getMinSize();

	default Integer readMetalAlloySizeNbtData(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();
		if(nbt.contains("nuggetSize")) return nbt.getInt("nuggetSize");
		writeMetalAlloySizeNbtData(itemStack, getMaxSize());
		return getMaxSize();
	}

	default boolean writeMetalAlloySizeNbtData(ItemStack itemStack, int size)
	{
		CompoundTag nbt = itemStack.getOrCreateTag();
		if (size > getMaxSize() || size < getMinSize()) return false;
		nbt.putInt("nuggetSize", size);
		return true;
	}
}
