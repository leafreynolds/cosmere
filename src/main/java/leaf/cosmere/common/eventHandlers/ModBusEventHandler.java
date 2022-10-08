/*
 * File updated ~ 5 - 5 - 2021 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEventHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		final Attribute xpRate = AttributesRegistry.XP_RATE_ATTRIBUTE.getAttribute();
		final Attribute nightVision = AttributesRegistry.NIGHT_VISION_ATTRIBUTE.getAttribute();
		final Attribute sizeAttr = AttributesRegistry.SIZE_ATTRIBUTE.get();

		event.add(EntityType.PLAYER, xpRate);
		event.add(EntityType.PLAYER, nightVision);
		event.add(EntityType.PLAYER, sizeAttr);

	}
}
