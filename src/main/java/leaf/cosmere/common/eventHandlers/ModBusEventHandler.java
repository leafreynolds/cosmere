/*
 * File updated ~ 7 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, AttributesRegistry.XP_RATE_ATTRIBUTE.getAttribute());
		event.add(EntityType.PLAYER, AttributesRegistry.NIGHT_VISION_ATTRIBUTE.getAttribute());
		event.add(EntityType.PLAYER, AttributesRegistry.SIZE_ATTRIBUTE.get());

		for (EntityType entityType : ForgeRegistries.ENTITY_TYPES)
		{
			if (!entityType.is(CosmereTags.EntityTypes.HAS_SPIRITWEB))
			{
				continue;
			}

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
