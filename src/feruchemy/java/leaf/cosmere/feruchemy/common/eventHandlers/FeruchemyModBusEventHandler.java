/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.eventHandlers;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.cap.item.CosmereItemCapabilities;
import leaf.cosmere.common.charge.IChargeable;
import leaf.cosmere.common.datamaps.CosmereDataMaps;
import leaf.cosmere.common.datamaps.MetalmindProperties;
import leaf.cosmere.common.eventHandlers.ModBusEventHandler;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.items.DataMapMetalmindBehavior;
import leaf.cosmere.feruchemy.common.items.MetalmindCurio;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurioItem;


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

	@SubscribeEvent
	public static void registerDataMaps(RegisterDataMapTypesEvent event)
	{
		event.register(CosmereDataMaps.METALMIND);
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event)
	{
		//the data map is empty until datapacks load, so register every item and check for an
		//entry at query time. item caps aren't cached, so /reload is picked up straight away
		for (Item item : BuiltInRegistries.ITEM)
		{
			if (!(item instanceof IChargeable))
			{
				event.registerItem(
						CosmereItemCapabilities.CHARGEABLE,
						(stack, context) ->
						{
							MetalmindProperties properties = CosmereDataMaps.getMetalmindProperties(stack.getItem());
							return properties != null ? new DataMapMetalmindBehavior(properties) : null;
						},
						item);
			}

			//curios wrapper for data-map metalminds
			//curios mod will sort ICurioItem items itself
			if (!(item instanceof ICurioItem))
			{
				event.registerItem(
						CuriosCapability.ITEM,
						(stack, context) ->
						{
							MetalmindProperties properties = CosmereDataMaps.getMetalmindProperties(stack.getItem());
							return properties != null
							       ? new MetalmindCurio(stack, new DataMapMetalmindBehavior(properties)) : null;
						},
						item);
			}
		}
	}
}
