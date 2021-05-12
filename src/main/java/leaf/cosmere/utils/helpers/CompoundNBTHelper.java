/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Thank you botania! Pretty much directly copied from botania as this is so much easier to work with!
 */

package leaf.cosmere.utils.helpers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public final class CompoundNBTHelper
{

    private static final int[] EMPTY_INT_ARRAY = new int[0];


    public static void set(CompoundNBT compoundTag, String tag, INBT nbt)
    {
        compoundTag.put(tag, nbt);
    }

    public static void setBoolean(CompoundNBT compoundTag, String tag, boolean b)
    {
        compoundTag.putBoolean(tag, b);
    }

    public static void setByte(CompoundNBT compoundTag, String tag, byte b)
    {
        compoundTag.putByte(tag, b);
    }

    public static void setShort(CompoundNBT compoundTag, String tag, short s)
    {
        compoundTag.putShort(tag, s);
    }

    public static void setInt(CompoundNBT compoundTag, String tag, int i)
    {
        compoundTag.putInt(tag, i);
    }

    public static void setIntArray(CompoundNBT compoundTag, String tag, int[] val)
    {
        compoundTag.putIntArray(tag, val);
    }
    public static void setIntArray(CompoundNBT compoundTag, String tag, List<Integer> val)
    {
        compoundTag.putIntArray(tag, val);
    }

    public static void setLong(CompoundNBT compoundTag, String tag, long l)
    {
        compoundTag.putLong(tag, l);
    }

    public static void setFloat(CompoundNBT compoundTag, String tag, float f)
    {
        compoundTag.putFloat(tag, f);
    }

    public static void setDouble(CompoundNBT compoundTag, String tag, double d)
    {
        compoundTag.putDouble(tag, d);
    }

    public static void setCompound(CompoundNBT compoundTag, String tag, CompoundNBT cmp)
    {
        if (!tag.equalsIgnoreCase("ench")) // not override the enchantments
        {
            compoundTag.put(tag, cmp);
        }
    }

    public static void setString(CompoundNBT compoundTag, String tag, String s)
    {
        compoundTag.putString(tag, s);
    }

    public static void setUuid(CompoundNBT compoundTag, String tag, UUID value)
    {
        compoundTag.putUniqueId(tag, value);
    }

    public static void setList(CompoundNBT compoundTag, String tag, ListNBT list)
    {
        compoundTag.put(tag, list);
    }

    public static void removeEntry(CompoundNBT compoundTag, String tag)
    {
        compoundTag.remove(tag);
    }

    // GETTERS

    public static boolean verifyExistance(CompoundNBT compoundTag, String tag)
    {
        return compoundTag.contains(tag);
    }

    @Nullable
    public static INBT get(CompoundNBT compoundTag, String tag)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.get(tag) : null;
    }

    public static boolean getBoolean(CompoundNBT compoundTag, String tag, boolean defaultExpected)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getBoolean(tag) : defaultExpected;
    }

    public static byte getByte(CompoundNBT compoundTag, String tag, byte defaultExpected)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getByte(tag) : defaultExpected;
    }

    public static short getShort(CompoundNBT compoundTag, String tag, short defaultExpected)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getShort(tag) : defaultExpected;
    }

    public static int getInt(CompoundNBT compoundTag, String tag, int defaultExpected)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getInt(tag) : defaultExpected;
    }

    public static int[] getIntArray(CompoundNBT compoundTag, String tag)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getIntArray(tag) : EMPTY_INT_ARRAY;
    }

    public static long getLong(CompoundNBT compoundTag, String tag, long defaultExpected)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getLong(tag) : defaultExpected;
    }

    public static float getFloat(CompoundNBT compoundTag, String tag, float defaultExpected)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getFloat(tag) : defaultExpected;
    }

    public static double getDouble(CompoundNBT compoundTag, String tag, double defaultExpected)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getDouble(tag) : defaultExpected;
    }

    /**
     * If nullifyOnFail is true it'll return null if it doesn't find any
     * compounds, otherwise it'll return a new one.
     **/
    public static CompoundNBT getCompound(CompoundNBT compoundTag, String tag, boolean nullifyOnFail)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getCompound(tag)
                                           : nullifyOnFail ? null : new CompoundNBT();
    }

    public static String getString(CompoundNBT compoundTag, String tag, String defaultExpected)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getString(tag) : defaultExpected;
    }

    @Nullable
    public static UUID getUuid(CompoundNBT compoundTag, String tag)
    {
        return verifyExistance(compoundTag, tag + "Most") && verifyExistance(compoundTag, tag + "Least")
               ? compoundTag.getUniqueId(tag) : null;
    }

    public static ListNBT getList(CompoundNBT compoundTag, String tag, int objtype, boolean nullifyOnFail)
    {
        return verifyExistance(compoundTag, tag) ? compoundTag.getList(tag, objtype)
                                           : nullifyOnFail ? null : new ListNBT();
    }
    /**
     * Returns true if the `target` tag contains all of the tags and values present in the `template` tag. Recurses into
     * compound tags and matches all template keys and values; recurses into list tags and matches the template against
     * the first elements of target. Empty lists and compounds in the template will match target lists and compounds of
     * any size.
     */
    public static boolean matchTag(@Nullable INBT template, @Nullable INBT target)
    {
        if (template instanceof CompoundNBT && target instanceof CompoundNBT)
        {
            return matchTagCompound((CompoundNBT) template, (CompoundNBT) target);
        }
        else if (template instanceof ListNBT && target instanceof ListNBT)
        {
            return matchTagList((ListNBT) template, (ListNBT) target);
        }
        else
        {
            return template == null || (target != null && target.equals(template));
        }
    }

    private static boolean matchTagCompound(CompoundNBT template, CompoundNBT target)
    {
        if (template.size() > target.size())
        {
            return false;
        }

        for (String key : template.keySet())
        {
            if (!matchTag(template.get(key), target.get(key)))
            {
                return false;
            }
        }

        return true;
    }

    private static boolean matchTagList(ListNBT template, ListNBT target)
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

    public static void renameTag(CompoundNBT nbt, String oldName, String newName)
    {
        INBT tag = nbt.get(oldName);
        if (tag != null)
        {
            nbt.remove(oldName);
            nbt.put(newName, tag);
        }
    }
}
