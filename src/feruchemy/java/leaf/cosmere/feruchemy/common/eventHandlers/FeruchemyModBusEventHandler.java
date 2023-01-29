/*
 * File updated ~ 2 - 11 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.eventHandlers;

import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Feruchemy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FeruchemyModBusEventHandler
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
				if (metalType.hasAssociatedManifestation() && FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.containsKey(metalType))
				{
					event.add(entityType, FeruchemyAttributes.FERUCHEMY_ATTRIBUTES.get(metalType).get());
				}
			}

		}
	}
}
