/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.curios;

import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

public class PatchouliCompat
{
    private static boolean patchouliModDetected;

    public static boolean PatchouliIsPresent()
    {
        return patchouliModDetected;
    }

    public static void init()
    {
        patchouliModDetected = ModList.get().isLoaded("patchouli");
        LogHelper.info("Patchouli detected, cosmere can use it's guide item.");
    }

}
