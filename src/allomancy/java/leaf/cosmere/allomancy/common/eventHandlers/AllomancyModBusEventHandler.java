/*
 * File updated ~ 7 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyAttributes;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.eventHandlers.ModBusEventHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Allomancy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AllomancyModBusEventHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		for (EntityType entityType : ModBusEventHandler.ENTITIES_THAT_CAN_HAVE_POWERS)
		{
			for (Metals.MetalType metalType : Metals.MetalType.values())
			{
				if (metalType.hasAssociatedManifestation() && AllomancyAttributes.ALLOMANCY_ATTRIBUTES.containsKey(metalType))
				{
					event.add(entityType, AllomancyAttributes.ALLOMANCY_ATTRIBUTES.get(metalType).get());
				}
			}
		}

		event.add(EntityType.WARDEN, AllomancyAttributes.ALLOMANCY_ATTRIBUTES.get(Metals.MetalType.BRONZE).get());
	}
}
