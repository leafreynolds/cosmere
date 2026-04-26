/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery.common.eventHandlers;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = Sandmastery.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SandmasteryModBusEventHandler
{
	private static Holder<Attribute> holder(Attribute attribute)
	{
		return BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, holder(SandmasteryAttributes.RIBBONS.getAttribute()));
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event)
	{
		event.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK,
				SandmasteryBlockEntitiesRegistry.SAND_SPREADER_BE.get(),
				(be, side) -> be.getItemHandler()
		);
	}
}
