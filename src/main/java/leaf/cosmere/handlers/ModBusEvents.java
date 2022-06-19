/*
 * File created ~ 5 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents
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
				if (metalType.hasAssociatedManifestation())
				{
					RegistryObject<Attribute> mistingAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getAllomancyRegistryName());
					RegistryObject<Attribute> ferringAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getFeruchemyRegistryName());

					event.add(entityType, mistingAttribute.get());
					event.add(entityType, ferringAttribute.get());
				}
				if (metalType.hasAttribute())
				{
					//check for others
					final RegistryObject<Attribute> metalRelatedAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getName());
					if (metalRelatedAttribute != null && metalRelatedAttribute.isPresent())
					{
						//players get everything, all others get atium
						if (entityType == EntityType.PLAYER || metalType == Metals.MetalType.ATIUM)
						{
							event.add(entityType, metalRelatedAttribute.get());
						}
					}
				}
			}
		}
	}
}
