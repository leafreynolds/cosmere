package leaf.cosmere.sandmastery.common.eventHandlers;

import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.blocks.entities.SandSpreaderBE;
import leaf.cosmere.sandmastery.common.items.sandpouch.SandpouchItemHandler;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlockEntitiesRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = Sandmastery.MODID)
public class SandmasteryModBusEventHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, SandmasteryAttributes.RIBBONS.getHolder());
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event)
	{
		event.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK,
				SandmasteryBlockEntitiesRegistry.SAND_SPREADER_BE.get(),
				(SandSpreaderBE blockEntity, Direction side) -> blockEntity.getItemHandler());

		event.registerItem(
				Capabilities.ItemHandler.ITEM,
				(stack, context) -> new SandpouchItemHandler(stack),
				SandmasteryItems.SAND_POUCH_ITEM.get());
	}
}
