/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.curios;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

public class CuriosCompat
{
    private static boolean curiosModDetected;


    public static boolean CuriosIsPresent()
    {
        return curiosModDetected;
    }

    public static void init()
    {
        curiosModDetected = ModList.get().isLoaded("curios");

        if (!curiosModDetected)
        {
            return;
        }

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(CuriosCompat::onEnqueueIMC);
    }


    private static void onEnqueueIMC(InterModEnqueueEvent event)
    {
        if (!curiosModDetected)
        {
            return;
        }

        SlotTypePreset[] oneSlot = {
                SlotTypePreset.BACK,
                SlotTypePreset.BELT,
                SlotTypePreset.BODY,
                SlotTypePreset.HEAD,
                SlotTypePreset.NECKLACE,
        };

        for (SlotTypePreset type : oneSlot)
        {
            InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> type.getMessageBuilder().build());
        }

        SlotTypePreset[] twoSlot = {
                SlotTypePreset.HANDS,
                SlotTypePreset.RING,
                SlotTypePreset.CHARM,
                SlotTypePreset.BRACELET,
                SlotTypePreset.NECKLACE,
        };

        for (SlotTypePreset type : twoSlot)
        {
            InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> type.getMessageBuilder().size(2).build());
        }

    }
}
