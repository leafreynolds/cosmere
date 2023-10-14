/*
 * File updated ~ 7 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.eventHandlers;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.eventHandlers.ModBusEventHandler;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Feruchemy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FeruchemyModBusEventHandler
{


	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		for (EntityType entityType : ModBusEventHandler.ENTITIES_THAT_CAN_HAVE_POWERS)
		{
			for (Metals.MetalType metalType : Metals.MetalType.values())
			{
				if (metalType.hasAssociatedManifestation() && FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.containsKey(metalType))
				{
					event.add(entityType, FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.get(metalType).get());
				}
			}

		}
	}
}
