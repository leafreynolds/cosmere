/*
 * File updated ~ 2 - 11 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.eventHandlers;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyAttributes;
import leaf.cosmere.api.Metals;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Allomancy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AllomancyModBusEventHandler
{
	private final static EntityType[] entityTypes = {
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
		for (EntityType entityType : entityTypes)
		{
			for (Metals.MetalType metalType : Metals.MetalType.values())
			{
				if (metalType.hasAssociatedManifestation() && AllomancyAttributes.ALLOMANCY_ATTRIBUTES.containsKey(metalType))
				{
					event.add(entityType, AllomancyAttributes.ALLOMANCY_ATTRIBUTES.get(metalType).get());
				}
			}
		}
	}
}
