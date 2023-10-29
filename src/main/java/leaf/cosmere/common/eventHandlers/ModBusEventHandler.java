/*
 * File updated ~ 26 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler
{
	//one place that multiple sub mods can reference?
	public final static EntityType[] ENTITIES_THAT_CAN_HAVE_POWERS = {
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


	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, AttributesRegistry.XP_RATE_ATTRIBUTE.getAttribute());
		event.add(EntityType.PLAYER, AttributesRegistry.NIGHT_VISION_ATTRIBUTE.getAttribute());
		event.add(EntityType.PLAYER, AttributesRegistry.SIZE_ATTRIBUTE.get());

		for (EntityType entityType : ModBusEventHandler.ENTITIES_THAT_CAN_HAVE_POWERS)
		{
			event.add(entityType, AttributesRegistry.COGNITIVE_CONCEALMENT.get());
			event.add(entityType, AttributesRegistry.CONNECTION.get());
			event.add(entityType, AttributesRegistry.COSMERE_FORTUNE.get());
			event.add(entityType, AttributesRegistry.IDENTITY.get());
			event.add(entityType, AttributesRegistry.DETERMINATION.get());
			event.add(entityType, AttributesRegistry.WARMTH.get());
			event.add(entityType, AttributesRegistry.HEALING_STRENGTH.get());
		}
	}
}
