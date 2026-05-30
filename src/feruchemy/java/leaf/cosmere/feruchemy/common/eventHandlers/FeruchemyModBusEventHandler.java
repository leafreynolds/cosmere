/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.eventHandlers;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.eventHandlers.ModBusEventHandler;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;


@EventBusSubscriber(modid = Feruchemy.MODID)
public class FeruchemyModBusEventHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		for (EntityType entityType : ModBusEventHandler.ENTITIES_THAT_CAN_HAVE_POWERS)
		{
			for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
			{
				if (metalType.hasAssociatedManifestation() && FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.containsKey(metalType))
				{
					event.add(entityType, FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.get(metalType).getHolder());
				}
			}
		}
	}
}
