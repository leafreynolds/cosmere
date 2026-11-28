/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.eventHandlers;

import leaf.cosmere.common.cap.item.CosmereItemCapabilities;
import leaf.cosmere.common.charge.IChargeable;
import leaf.cosmere.common.datamaps.CosmereDataMaps;
import leaf.cosmere.common.datamaps.SpikeProperties;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.capabilities.HemalurgyItemCapabilities;
import leaf.cosmere.hemalurgy.common.entity.Koloss;
import leaf.cosmere.hemalurgy.common.items.DataMapSpikeBehavior;
import leaf.cosmere.hemalurgy.common.items.IHemalurgicInfo;
import leaf.cosmere.hemalurgy.common.items.SpikeCurio;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyAttributes;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyEntityTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurioItem;


@EventBusSubscriber(modid = Hemalurgy.MODID)
public class HemalurgyModBusEventHandler
{

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, HemalurgyAttributes.SPIRITWEB_INTEGRITY.getHolder());
	}


	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event)
	{
		event.put(HemalurgyEntityTypes.KOLOSS_LARGE.get(), Koloss.largeAttributes().build());
		event.put(HemalurgyEntityTypes.KOLOSS_MEDIUM.get(), Koloss.mediumAttributes().build());
		event.put(HemalurgyEntityTypes.KOLOSS_SMALL.get(), Koloss.smallAttributes().build());
	}

	@SubscribeEvent
	public static void registerDataMaps(RegisterDataMapTypesEvent event)
	{
		event.register(CosmereDataMaps.SPIKE);
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event)
	{
		//the data map is empty until datapacks load, so register every item and check for an
		//entry at query time. item caps aren't cached, so /reload is picked up straight away
		for (Item item : BuiltInRegistries.ITEM)
		{
			if (item instanceof IHemalurgicInfo)
			{
				event.registerItem(
						HemalurgyItemCapabilities.SPIKE,
						(stack, context) -> (IHemalurgicInfo) stack.getItem(),
						item);
				continue;
			}

			event.registerItem(
					HemalurgyItemCapabilities.SPIKE,
					(stack, context) ->
					{
						SpikeProperties properties = HemalurgyItemCapabilities.getSpikeProperties(stack.getItem());
						return properties != null ? new DataMapSpikeBehavior(properties) : null;
					},
					item);

			//data-map spikes hold charge too. IChargeable items are covered by main
			if (!(item instanceof IChargeable))
			{
				event.registerItem(
						CosmereItemCapabilities.CHARGEABLE,
						(stack, context) ->
						{
							SpikeProperties properties = HemalurgyItemCapabilities.getSpikeProperties(stack.getItem());
							return properties != null ? new DataMapSpikeBehavior(properties) : null;
						},
						item);
			}

			//curios wrapper for data-map spikes. Curios covers ICurioItem items itself
			if (!(item instanceof ICurioItem))
			{
				event.registerItem(
						CuriosCapability.ITEM,
						(stack, context) ->
						{
							SpikeProperties properties = HemalurgyItemCapabilities.getSpikeProperties(stack.getItem());
							return properties != null ? new SpikeCurio(stack, new DataMapSpikeBehavior(properties)) : null;
						},
						item);
			}
		}
	}
}
