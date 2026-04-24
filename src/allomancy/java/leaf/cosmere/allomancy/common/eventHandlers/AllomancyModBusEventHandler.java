/*
 * File updated ~ 2026-04-25 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyAttributes;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.eventHandlers.ModBusEventHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;


@EventBusSubscriber(modid = Allomancy.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AllomancyModBusEventHandler
{
	private static Holder<Attribute> holder(Attribute attribute)
	{
		return BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		for (EntityType<? extends LivingEntity> entityType : ModBusEventHandler.ENTITIES_THAT_CAN_HAVE_POWERS)
		{
			for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
			{
				if (metalType.hasAssociatedManifestation() && AllomancyAttributes.ALLOMANCY_ATTRIBUTES.containsKey(metalType))
				{
					event.add(entityType, holder(AllomancyAttributes.ALLOMANCY_ATTRIBUTES.get(metalType).get()));
				}
			}
		}

		event.add(EntityType.WARDEN, holder(AllomancyAttributes.ALLOMANCY_ATTRIBUTES.get(Metals.MetalType.BRONZE).get()));
	}
}
