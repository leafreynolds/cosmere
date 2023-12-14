/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.eventHandlers;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.entity.Koloss;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyAttributes;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HemalurgyModBusEventHandler
{

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		event.add(EntityType.PLAYER, HemalurgyAttributes.SPIRITWEB_INTEGRITY.getAttribute());
	}


	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event)
	{
		event.put(HemalurgyEntityTypes.KOLOSS_LARGE.get(), Koloss.largeAttributes().build());
		event.put(HemalurgyEntityTypes.KOLOSS_MEDIUM.get(), Koloss.mediumAttributes().build());
		event.put(HemalurgyEntityTypes.KOLOSS_SMALL.get(), Koloss.smallAttributes().build());
	}
}
