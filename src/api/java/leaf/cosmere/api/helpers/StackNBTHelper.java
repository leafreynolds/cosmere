/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * In 1.21.x ItemStack#getOrCreateTag() is gone — per-stack NBT moved to the DataComponents system.
 * For a drop-in port (callers across allomancy/feruchemy/hemalurgy/sandmastery/main still use this
 * helper unchanged), all get/set/remove operations here route through DataComponents.CUSTOM_DATA,
 * which wraps a CompoundTag. Phase 7 should migrate each feature's state to a purpose-built
 * DataComponentType (strength/charge-level/nuggetSize/...). serializeStack now needs a
 * HolderLookup.Provider and is stubbed until its feruchemy datagen caller is ported.
 */

package leaf.cosmere.api.helpers;


import com.google.gson.JsonObject;
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

	private static CompoundTag read(ItemStack stack)
	{
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
	}

	private static void mutate(ItemStack stack, Consumer<CompoundTag> mutator)
	{
		CustomData.update(DataComponents.CUSTOM_DATA, stack, mutator);
	}

	// SETTERS ///////////////////////////////////////////////////////////////////

	public static void set(ItemStack stack, String tag, Tag nbt)
	{
		mutate(stack, t -> t.put(tag, nbt));
	}

	public static void setBoolean(ItemStack stack, String tag, boolean b)
	{
		mutate(stack, t -> t.putBoolean(tag, b));
	}

	public static void setByte(ItemStack stack, String tag, byte b)
	{
		mutate(stack, t -> t.putByte(tag, b));
	}

	public static void setShort(ItemStack stack, String tag, short s)
	{
		mutate(stack, t -> t.putShort(tag, s));
	}

	public static void setInt(ItemStack stack, String tag, int i)
	{
		mutate(stack, t -> t.putInt(tag, i));
	}

	public static void setIntArray(ItemStack stack, String tag, int[] val)
	{
		mutate(stack, t -> t.putIntArray(tag, val));
	}

	public static void setLong(ItemStack stack, String tag, long l)
	{
		mutate(stack, t -> t.putLong(tag, l));
	}

	public static void setFloat(ItemStack stack, String tag, float f)
	{
		mutate(stack, t -> t.putFloat(tag, f));
	}

	public static void setDouble(ItemStack stack, String tag, double d)
	{
		mutate(stack, t -> t.putDouble(tag, d));
	}

	public static void setCompound(ItemStack stack, String tag, CompoundTag cmp)
	{
		if (!tag.equalsIgnoreCase("ench")) // not override the enchantments
		{
			mutate(stack, t -> t.put(tag, cmp));
		}
	}

	public static void setString(ItemStack stack, String tag, String s)
	{
		mutate(stack, t -> t.putString(tag, s));
	}

	public static void setUuid(ItemStack stack, String tag, UUID value)
	{
		mutate(stack, t -> t.putUUID(tag, value));
	}

	public static void setList(ItemStack stack, String tag, ListTag list)
	{
		mutate(stack, t -> t.put(tag, list));
	}

	public static void removeEntry(ItemStack stack, String tag)
	{
		mutate(stack, t -> t.remove(tag));
	}

	// GETTERS ///////////////////////////////////////////////////////////////////

	public static boolean verifyExistance(ItemStack stack, String tag)
	{
		return !stack.isEmpty() && read(stack).contains(tag);
	}

	@Nullable
	public static Tag get(ItemStack stack, String tag)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.get(tag) : null;
	}

	public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getBoolean(tag) : defaultExpected;
	}

	public static byte getByte(ItemStack stack, String tag, byte defaultExpected)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getByte(tag) : defaultExpected;
	}

	public static short getShort(ItemStack stack, String tag, short defaultExpected)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getShort(tag) : defaultExpected;
	}

	public static int getInt(ItemStack stack, String tag, int defaultExpected)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getInt(tag) : defaultExpected;
	}

	public static int[] getIntArray(ItemStack stack, String tag)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getIntArray(tag) : EMPTY_INT_ARRAY;
	}

	public static long getLong(ItemStack stack, String tag, long defaultExpected)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getLong(tag) : defaultExpected;
	}

	public static float getFloat(ItemStack stack, String tag, float defaultExpected)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getFloat(tag) : defaultExpected;
	}

	public static double getDouble(ItemStack stack, String tag, double defaultExpected)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getDouble(tag) : defaultExpected;
	}

	/**
	 * If nullifyOnFail is true it'll return null if it doesn't find any
	 * compounds, otherwise it'll return a new one.
	 **/
	public static CompoundTag getCompound(ItemStack stack, String tag, boolean nullifyOnFail)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getCompound(tag) : nullifyOnFail ? null : new CompoundTag();
	}

	public static String getString(ItemStack stack, String tag, String defaultExpected)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getString(tag) : defaultExpected;
	}

	@Nullable
	public static UUID getUuid(ItemStack stack, String tag)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getUUID(tag) : null;
	}

	public static ListTag getList(ItemStack stack, String tag, int objtype, boolean nullifyOnFail)
	{
		CompoundTag t = read(stack);
		return t.contains(tag) ? t.getList(tag, objtype) : nullifyOnFail ? null : new ListTag();
	}

	/**
	 * Serializes the given stack for recipe JSON. Phase 7 note: the 1.21 replacement
	 * (ItemStack#save(HolderLookup.Provider)) requires a registry lookup that callers at the
	 * api level don't have. Returns null until the feruchemy recipe-result provider that uses
	 * this is ported.
	 */
	public static JsonObject serializeStack(ItemStack stack)
	{
		// TODO(Phase 7): rebuild via ItemStack.CODEC or a datagen-context registry lookup.
		return null;
	}

	/**
	 * Returns true if the `target` tag contains all of the tags and values present in the `template` tag. Recurses into
	 * compound tags and matches all template keys and values; recurses into list tags and matches the template against
	 * the first elements of target. Empty lists and compounds in the template will match target lists and compounds of
	 * any size.
	 */
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
		if (template.size() > target.size())
		{
			return false;
		}

		for (String key : template.getAllKeys())
		{
			if (!matchTag(template.get(key), target.get(key)))
			{
				return false;
			}
		}

		return true;
	}

	private static boolean matchTagList(ListTag template, ListTag target)
	{
		if (template.size() > target.size())
		{
			return false;
		}

		for (int i = 0; i < template.size(); i++)
		{
			if (!matchTag(template.get(i), target.get(i)))
			{
				return false;
			}
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
