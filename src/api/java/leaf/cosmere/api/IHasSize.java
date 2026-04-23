package leaf.cosmere.api;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public interface IHasSize
{
	int getMaxSize();

	int getMinSize();

	// Phase 7 will migrate nuggetSize to a dedicated DataComponent; for now it rides in CUSTOM_DATA
	// so we don't need to register a new component type during the api compile pass.
	String NUGGET_SIZE_KEY = "nuggetSize";

	default Integer readMetalAlloySizeNbtData(ItemStack itemStack)
	{
		CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if (nbt.contains(NUGGET_SIZE_KEY))
		{
			return nbt.getInt(NUGGET_SIZE_KEY);
		}
		writeMetalAlloySizeNbtData(itemStack, getMaxSize());
		return getMaxSize();
	}

	default boolean writeMetalAlloySizeNbtData(ItemStack itemStack, int size)
	{
		if (size > getMaxSize() || size < getMinSize())
		{
			return false;
		}
		CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.putInt(NUGGET_SIZE_KEY, size));
		return true;
	}
}
