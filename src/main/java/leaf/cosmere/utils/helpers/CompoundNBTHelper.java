/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Thank you botania! Pretty much directly copied from botania as this is so much easier to work with!
 */

package leaf.cosmere.utils.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public final class CompoundNBTHelper
{

	private static final int[] EMPTY_INT_ARRAY = new int[0];


	public static void set(CompoundTag compoundTag, String tag, Tag nbt)
	{
		compoundTag.put(tag, nbt);
	}

	public static void setBoolean(CompoundTag compoundTag, String tag, boolean b)
	{
		compoundTag.putBoolean(tag, b);
	}

	public static void setByte(CompoundTag compoundTag, String tag, byte b)
	{
		compoundTag.putByte(tag, b);
	}

	public static void setShort(CompoundTag compoundTag, String tag, short s)
	{
		compoundTag.putShort(tag, s);
	}

	public static void setInt(CompoundTag compoundTag, String tag, int i)
	{
		compoundTag.putInt(tag, i);
	}

	public static void setIntArray(CompoundTag compoundTag, String tag, int[] val)
	{
		compoundTag.putIntArray(tag, val);
	}

	public static void setIntArray(CompoundTag compoundTag, String tag, List<Integer> val)
	{
		compoundTag.putIntArray(tag, val);
	}

	public static void setLong(CompoundTag compoundTag, String tag, long l)
	{
		compoundTag.putLong(tag, l);
	}

	public static void setFloat(CompoundTag compoundTag, String tag, float f)
	{
		compoundTag.putFloat(tag, f);
	}

	public static void setDouble(CompoundTag compoundTag, String tag, double d)
	{
		compoundTag.putDouble(tag, d);
	}

	public static void setCompound(CompoundTag compoundTag, String tag, CompoundTag cmp)
	{
		if (!tag.equalsIgnoreCase("ench")) // not override the enchantments
		{
			compoundTag.put(tag, cmp);
		}
	}

	public static void setString(CompoundTag compoundTag, String tag, String s)
	{
		compoundTag.putString(tag, s);
	}

	public static void setUuid(CompoundTag compoundTag, String tag, UUID value)
	{
		compoundTag.putUUID(tag, value);
	}

	public static void setList(CompoundTag compoundTag, String tag, ListTag list)
	{
		compoundTag.put(tag, list);
	}

	public static void removeEntry(CompoundTag compoundTag, String tag)
	{
		compoundTag.remove(tag);
	}

	// GETTERS

	public static boolean verifyExistance(CompoundTag compoundTag, String tag)
	{
		return compoundTag.contains(tag);
	}

	@Nullable
	public static Tag get(CompoundTag compoundTag, String tag)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.get(tag) : null;
	}

	public static boolean getBoolean(CompoundTag compoundTag, String tag, boolean defaultExpected)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getBoolean(tag) : defaultExpected;
	}

	public static byte getByte(CompoundTag compoundTag, String tag, byte defaultExpected)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getByte(tag) : defaultExpected;
	}

	public static short getShort(CompoundTag compoundTag, String tag, short defaultExpected)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getShort(tag) : defaultExpected;
	}

	public static int getInt(CompoundTag compoundTag, String tag, int defaultExpected)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getInt(tag) : defaultExpected;
	}

	public static int[] getIntArray(CompoundTag compoundTag, String tag)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getIntArray(tag) : EMPTY_INT_ARRAY;
	}

	public static long getLong(CompoundTag compoundTag, String tag, long defaultExpected)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getLong(tag) : defaultExpected;
	}

	public static float getFloat(CompoundTag compoundTag, String tag, float defaultExpected)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getFloat(tag) : defaultExpected;
	}

	public static double getDouble(CompoundTag compoundTag, String tag, double defaultExpected)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getDouble(tag) : defaultExpected;
	}

	/**
	 * If nullifyOnFail is true it'll return null if it doesn't find any
	 * compounds, otherwise it'll return a new one.
	 **/
	public static CompoundTag getCompound(CompoundTag compoundTag, String tag, boolean nullifyOnFail)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getCompound(tag)
		                                         : nullifyOnFail ? null : new CompoundTag();
	}

	public static String getString(CompoundTag compoundTag, String tag, String defaultExpected)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getString(tag) : defaultExpected;
	}

	@Nullable
	public static UUID getUuid(CompoundTag compoundTag, String tag)
	{
		return verifyExistance(compoundTag, tag + "Most") && verifyExistance(compoundTag, tag + "Least")
		       ? compoundTag.getUUID(tag) : null;
	}

	public static ListTag getList(CompoundTag compoundTag, String tag, int objtype, boolean nullifyOnFail)
	{
		return verifyExistance(compoundTag, tag) ? compoundTag.getList(tag, objtype)
		                                         : nullifyOnFail ? null : new ListTag();
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
