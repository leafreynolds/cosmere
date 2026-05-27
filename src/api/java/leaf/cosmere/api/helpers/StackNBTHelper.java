package leaf.cosmere.api.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Consumer;

public final class StackNBTHelper
{
	private static final int[] EMPTY_INT_ARRAY = new int[0];

	// INTERNAL COMPONENT HELPERS ////////////////////////////////////////////////

	private static void mutateData(ItemStack stack, Consumer<CompoundTag> mutator)
	{
		CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		mutator.accept(tag);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
	}

	private static CompoundTag readData(ItemStack stack)
	{
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).getUnsafe();
	}

	// SETTERS ///////////////////////////////////////////////////////////////////

	public static void set(ItemStack stack, String tag, Tag nbt)
	{
		mutateData(stack, t -> t.put(tag, nbt));
	}

	public static void setBoolean(ItemStack stack, String tag, boolean b)
	{
		mutateData(stack, t -> t.putBoolean(tag, b));
	}

	public static void setByte(ItemStack stack, String tag, byte b)
	{
		mutateData(stack, t -> t.putByte(tag, b));
	}

	public static void setShort(ItemStack stack, String tag, short s)
	{
		mutateData(stack, t -> t.putShort(tag, s));
	}

	public static void setInt(ItemStack stack, String tag, int i)
	{
		mutateData(stack, t -> t.putInt(tag, i));
	}

	public static void setIntArray(ItemStack stack, String tag, int[] val)
	{
		mutateData(stack, t -> t.putIntArray(tag, val));
	}

	public static void setLong(ItemStack stack, String tag, long l)
	{
		mutateData(stack, t -> t.putLong(tag, l));
	}

	public static void setFloat(ItemStack stack, String tag, float f)
	{
		mutateData(stack, t -> t.putFloat(tag, f));
	}

	public static void setDouble(ItemStack stack, String tag, double d)
	{
		mutateData(stack, t -> t.putDouble(tag, d));
	}

	public static void setCompound(ItemStack stack, String tag, CompoundTag cmp)
	{
		if (!tag.equalsIgnoreCase("ench")) // enchantments are now a separate component, but keeping your logic
		{
			mutateData(stack, t -> t.put(tag, cmp));
		}
	}

	public static void setString(ItemStack stack, String tag, String s)
	{
		mutateData(stack, t -> t.putString(tag, s));
	}

	public static void setUuid(ItemStack stack, String tag, UUID value)
	{
		mutateData(stack, t -> t.putUUID(tag, value));
	}

	public static void setList(ItemStack stack, String tag, ListTag list)
	{
		mutateData(stack, t -> t.put(tag, list));
	}

	public static void removeEntry(ItemStack stack, String tag)
	{
		mutateData(stack, t -> t.remove(tag));
	}

	// GETTERS ///////////////////////////////////////////////////////////////////

	public static boolean verifyExistance(ItemStack stack, String tag)
	{
		return !stack.isEmpty() && readData(stack).contains(tag);
	}

	@Nullable
	public static Tag get(ItemStack stack, String tag)
	{
		return verifyExistance(stack, tag) ? readData(stack).get(tag) : null;
	}

	public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected)
	{
		return verifyExistance(stack, tag) ? readData(stack).getBoolean(tag) : defaultExpected;
	}

	public static byte getByte(ItemStack stack, String tag, byte defaultExpected)
	{
		return verifyExistance(stack, tag) ? readData(stack).getByte(tag) : defaultExpected;
	}

	public static short getShort(ItemStack stack, String tag, short defaultExpected)
	{
		return verifyExistance(stack, tag) ? readData(stack).getShort(tag) : defaultExpected;
	}

	public static int getInt(ItemStack stack, String tag, int defaultExpected)
	{
		return verifyExistance(stack, tag) ? readData(stack).getInt(tag) : defaultExpected;
	}

	public static int[] getIntArray(ItemStack stack, String tag)
	{
		return verifyExistance(stack, tag) ? readData(stack).getIntArray(tag) : EMPTY_INT_ARRAY;
	}

	public static long getLong(ItemStack stack, String tag, long defaultExpected)
	{
		return verifyExistance(stack, tag) ? readData(stack).getLong(tag) : defaultExpected;
	}

	public static float getFloat(ItemStack stack, String tag, float defaultExpected)
	{
		return verifyExistance(stack, tag) ? readData(stack).getFloat(tag) : defaultExpected;
	}

	public static double getDouble(ItemStack stack, String tag, double defaultExpected)
	{
		return verifyExistance(stack, tag) ? readData(stack).getDouble(tag) : defaultExpected;
	}

	public static CompoundTag getCompound(ItemStack stack, String tag, boolean nullifyOnFail)
	{
		return verifyExistance(stack, tag) ? readData(stack).getCompound(tag)
		                                   : nullifyOnFail ? null : new CompoundTag();
	}

	public static String getString(ItemStack stack, String tag, String defaultExpected)
	{
		return verifyExistance(stack, tag) ? readData(stack).getString(tag) : defaultExpected;
	}

	@Nullable
	public static UUID getUuid(ItemStack stack, String tag)
	{
		return verifyExistance(stack, tag) ? readData(stack).getUUID(tag) : null;
	}

	public static ListTag getList(ItemStack stack, String tag, int objtype, boolean nullifyOnFail)
	{
		return verifyExistance(stack, tag) ? readData(stack).getList(tag, objtype)
		                                   : nullifyOnFail ? null : new ListTag();
	}

	public static JsonObject serializeStack(ItemStack stack, HolderLookup.Provider registries)
	{
		JsonElement element = ItemStack.CODEC.encodeStart(registries.createSerializationContext(JsonOps.INSTANCE), stack).getOrThrow();
		return element.getAsJsonObject();
	}

	// MATCHING UTILS ////////////////////////////////////////////////////////////

	public static boolean matchTag(@Nullable Tag template, @Nullable Tag target)
	{
		if (template instanceof CompoundTag && target instanceof CompoundTag)
		{
			return matchTagCompound((CompoundTag) template, (CompoundTag) target);
		}
		else if (template instanceof ListTag && target instanceof ListTag)
		{
			return matchTagList((ListTag) template, (ListTag) target);
		}
		else
		{
			return template == null || (target != null && target.equals(template));
		}
	}

	private static boolean matchTagCompound(CompoundTag template, CompoundTag target)
	{
		if (template.size() > target.size()) return false;
		for (String key : template.getAllKeys())
		{
			if (!matchTag(template.get(key), target.get(key))) return false;
		}
		return true;
	}

	private static boolean matchTagList(ListTag template, ListTag target)
	{
		if (template.size() > target.size()) return false;
		for (int i = 0; i < template.size(); i++)
		{
			if (!matchTag(template.get(i), target.get(i))) return false;
		}
		return true;
	}

	public static void renameTag(CompoundTag nbt, String oldName, String newName)
	{
		Tag tag = nbt.get(oldName);
		if (tag != null)
		{
			nbt.remove(oldName);
			nbt.put(newName, tag);
		}
	}
}
