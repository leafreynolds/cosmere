/*
 * File updated ~ 7 - 4 - 2026 ~ Leaf
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyAttributes;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = Allomancy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AllomancyModBusEventHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		for (EntityType entityType : ForgeRegistries.ENTITY_TYPES)
		{
			if (!entityType.is(CosmereTags.EntityTypes.HAS_SPIRITWEB))
			{
				continue;
			}

			for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
			{
				if (metalType.hasAssociatedManifestation() && AllomancyAttributes.ALLOMANCY_ATTRIBUTES.containsKey(metalType))
				{
					event.add(entityType, AllomancyAttributes.ALLOMANCY_ATTRIBUTES.get(metalType).get());
				}
			}
		}

		event.add(EntityType.WARDEN, AllomancyAttributes.ALLOMANCY_ATTRIBUTES.get(Metals.MetalType.BRONZE).get());
	}
}
