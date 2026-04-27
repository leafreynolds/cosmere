/*
 * File updated ~ 27 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.common.compat.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

public final class CustomDataSubtypeInterpreter implements ISubtypeInterpreter<ItemStack>
{
	public static final CustomDataSubtypeInterpreter ALL = new CustomDataSubtypeInterpreter(tag -> tag);

	private final UnaryOperator<CompoundTag> filter;

	public CustomDataSubtypeInterpreter(UnaryOperator<CompoundTag> filter)
	{
		this.filter = filter;
	}

	@Nullable
	@Override
	public Object getSubtypeData(ItemStack ingredient, UidContext context)
	{
		// Recipe lookups should match the base item, regardless of stored data.
		if (context == UidContext.Recipe)
		{
			return null;
		}
		CompoundTag tag = ingredient.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		CompoundTag filtered = filter.apply(tag);
		return filtered == null || filtered.isEmpty() ? null : filtered;
	}

	@Override
	public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context)
	{
		Object data = getSubtypeData(ingredient, context);
		return data == null ? "" : data.toString();
	}
}