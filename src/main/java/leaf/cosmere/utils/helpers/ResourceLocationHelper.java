/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import leaf.cosmere.Cosmere;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class ResourceLocationHelper
{
    public static ResourceLocation prefix(String path)
    {
        return new ResourceLocation(Cosmere.MODID, path.toLowerCase(Locale.ROOT));
    }
}
