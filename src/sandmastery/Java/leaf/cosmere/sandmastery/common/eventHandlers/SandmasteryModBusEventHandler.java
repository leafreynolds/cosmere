package leaf.cosmere.sandmastery.common.eventHandlers;

import leaf.cosmere.api.Taldain;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Sandmastery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SandmasteryModBusEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
    {
        for (Taldain.Investiture investiture : Taldain.Investiture.values())
        {
            event.add(EntityType.PLAYER, SandmasteryAttributes.SANDMASTER_ATTRIBUTES.get(investiture).getAttribute());
        }
    }
}

