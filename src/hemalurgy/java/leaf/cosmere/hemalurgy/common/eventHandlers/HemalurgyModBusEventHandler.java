/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.eventHandlers;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.entity.Koloss;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyAttributes;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyEntityTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;


@EventBusSubscriber(modid = Hemalurgy.MODID, bus = EventBusSubscriber.Bus.MOD)
public class HemalurgyModBusEventHandler
{

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, holder(HemalurgyAttributes.SPIRITWEB_INTEGRITY.getAttribute()));
	}


	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event)
	{
		event.put(HemalurgyEntityTypes.KOLOSS_LARGE.get(), Koloss.largeAttributes().build());
		event.put(HemalurgyEntityTypes.KOLOSS_MEDIUM.get(), Koloss.mediumAttributes().build());
		event.put(HemalurgyEntityTypes.KOLOSS_SMALL.get(), Koloss.smallAttributes().build());
	}

	private static Holder<Attribute> holder(Attribute attribute)
	{
		return BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
	}
}
