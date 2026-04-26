/*
 * File updated ~ 21 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.eventHandlers;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.common.entity.AviarBird;
import leaf.cosmere.aviar.common.registries.AviarAttributes;
import leaf.cosmere.aviar.common.registries.AviarEntityTypes;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;


@EventBusSubscriber(modid = Aviar.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AviarCommonModEvents
{

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, holder(AviarAttributes.HOSTILE_LIFE_SENSE.getAttribute()));
	}


	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event)
	{
		final AttributeSupplier.Builder attributes = AviarBird.createAttributes();
		final AttributeSupplier.Builder add = attributes.add(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(AttributesRegistry.COGNITIVE_CONCEALMENT.get()), 5);
		event.put(AviarEntityTypes.AVIAR_ENTITY.get(), add.build());
	}

	private static Holder<Attribute> holder(Attribute attribute)
	{
		return BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
	}
}
