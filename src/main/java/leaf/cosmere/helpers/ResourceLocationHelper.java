/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

/*
 * This class is distributed as part of the Cosmere Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Cosmere
 *
 * Cosmere is Open Source and distributed under the
 * Cosmere License: http://Cosmeremod.net/license.php
 */
package leaf.cosmere.helpers;

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
