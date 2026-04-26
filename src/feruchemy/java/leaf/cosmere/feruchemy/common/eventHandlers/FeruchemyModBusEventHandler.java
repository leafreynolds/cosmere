/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.eventHandlers;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.eventHandlers.ModBusEventHandler;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;


@EventBusSubscriber(modid = Feruchemy.MODID, bus = EventBusSubscriber.Bus.MOD)
public class FeruchemyModBusEventHandler
{


	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		for (EntityType<? extends LivingEntity> entityType : ModBusEventHandler.ENTITIES_THAT_CAN_HAVE_POWERS)
		{
			for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
			{
				if (metalType.hasAssociatedManifestation() && FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.containsKey(metalType))
				{
					event.add(entityType, BuiltInRegistries.ATTRIBUTE.wrapAsHolder(FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.get(metalType).get()));
				}
			}

		}
	}
}
