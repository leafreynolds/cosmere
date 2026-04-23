/*
 * File updated ~ 2026-04-23 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;


@EventBusSubscriber(modid = Cosmere.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler
{
	//one place that multiple sub mods can reference?
	@SuppressWarnings("unchecked")
	public final static EntityType<? extends LivingEntity>[] ENTITIES_THAT_CAN_HAVE_POWERS = new EntityType[] {
			EntityType.PLAYER,

			EntityType.VILLAGER,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.WANDERING_TRADER,

			EntityType.EVOKER,
			EntityType.ILLUSIONER,
			EntityType.PILLAGER,
			EntityType.VINDICATOR,
			EntityType.WITCH,

			EntityType.PIGLIN,
			EntityType.PIGLIN_BRUTE,

			EntityType.CAT,
			EntityType.LLAMA,
			EntityType.TRADER_LLAMA,
	};

	private static Holder<Attribute> holder(Attribute attribute)
	{
		return BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, holder(AttributesRegistry.XP_RATE_ATTRIBUTE.getAttribute()));
		event.add(EntityType.PLAYER, holder(AttributesRegistry.NIGHT_VISION_ATTRIBUTE.getAttribute()));
		event.add(EntityType.PLAYER, holder(AttributesRegistry.SIZE_ATTRIBUTE.get()));

		for (EntityType<? extends LivingEntity> entityType : ENTITIES_THAT_CAN_HAVE_POWERS)
		{
			event.add(entityType, holder(AttributesRegistry.COGNITIVE_CONCEALMENT.get()));
			event.add(entityType, holder(AttributesRegistry.CONNECTION.get()));
			event.add(entityType, holder(AttributesRegistry.COSMERE_FORTUNE.get()));
			event.add(entityType, holder(AttributesRegistry.IDENTITY.get()));
			event.add(entityType, holder(AttributesRegistry.DETERMINATION.get()));
			event.add(entityType, holder(AttributesRegistry.WARMTH.get()));
			event.add(entityType, holder(AttributesRegistry.HEALING_STRENGTH.get()));
		}
	}
}
