/*
 * File updated ~ 21 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.eventHandlers;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.common.entity.AviarBird;
import leaf.cosmere.aviar.common.registries.AviarAttributes;
import leaf.cosmere.aviar.common.registries.AviarEntityTypes;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Aviar.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AviarCommonModEvents
{

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, AviarAttributes.HOSTILE_LIFE_SENSE.getAttribute());
	}


	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event)
	{
		final AttributeSupplier.Builder attributes = AviarBird.createAttributes();
		final AttributeSupplier.Builder add = attributes.add(AttributesRegistry.COGNITIVE_CONCEALMENT.get(), 5);
		event.put(AviarEntityTypes.AVIAR_ENTITY.get(), add.build());
	}
}
