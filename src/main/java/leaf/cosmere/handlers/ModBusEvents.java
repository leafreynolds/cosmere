/*
 * File created ~ 5 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.util.Locale;


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
        for (EntityType entityType : entityTypes)
        {
            for (Metals.MetalType metalType : Metals.MetalType.values())
            {
                if (!metalType.hasAssociatedManifestation())
                {
                    continue;
                }

                RegistryObject<Attribute> mistingAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getMistingName());
                RegistryObject<Attribute> ferringAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getFerringName());

                event.add(entityType, mistingAttribute.get());
                event.add(entityType, ferringAttribute.get());

                //check for others
                final RegistryObject<Attribute> metalRelatedAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getName());

                if (metalRelatedAttribute != null && metalRelatedAttribute.isPresent())
                {
                    event.add(entityType, metalRelatedAttribute.get());
                }

            }
        }
    }
}
