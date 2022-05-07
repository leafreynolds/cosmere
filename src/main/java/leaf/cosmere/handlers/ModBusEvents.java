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
			EntityType.WANDERING_TRADER,

			EntityType.ZOMBIE_VILLAGER,
			EntityType.WITCH,

			EntityType.PILLAGER,
			EntityType.EVOKER,
			EntityType.ILLUSIONER,
			EntityType.VINDICATOR,

			EntityType.PIGLIN,

			EntityType.CAT,
			EntityType.LLAMA,
			EntityType.TRADER_LLAMA,
	};


	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasAssociatedManifestation())
			{
				for (EntityType entityType : entityTypes)
				{
					RegistryObject<Attribute> mistingAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getAllomancyRegistryName());
					RegistryObject<Attribute> ferringAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getFeruchemyRegistryName());

					event.add(entityType, mistingAttribute.get());
					event.add(entityType, ferringAttribute.get());
				}
			}

			{
				//check for others
				final RegistryObject<Attribute> metalRelatedAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getName());
				if (metalRelatedAttribute != null && metalRelatedAttribute.isPresent())
				{
					//player only, because it doesn't make sense for mobs to have them.
					//eg xp gain rate,
					event.add(EntityType.PLAYER, metalRelatedAttribute.get());
				}

			}
		}
	}
}
