package leaf.cosmere.surgebinding.common.eventHandlers;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.entity.Chull;
import leaf.cosmere.surgebinding.common.entity.spren.Cryptic;
import leaf.cosmere.surgebinding.common.entity.spren.Honorspren;
import leaf.cosmere.surgebinding.common.registries.SurgebindingAttributes;
import leaf.cosmere.surgebinding.common.registries.SurgebindingEntityTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = Surgebinding.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SurgebindingModEventHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
	{
		for (Roshar.Surges surge : EnumUtils.SURGES)
		{
			Attribute attr = SurgebindingAttributes.SURGEBINDING_ATTRIBUTES.get(surge).getAttribute();
			event.add((EntityType<? extends LivingEntity>) EntityType.PLAYER,
					BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attr));
		}
	}

	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event)
	{
		event.put(SurgebindingEntityTypes.CHULL.get(), Chull.createAttributes().build());
		event.put(SurgebindingEntityTypes.CRYPTIC.get(), Cryptic.createAttributes().build());
		event.put(SurgebindingEntityTypes.HONORSPREN.get(), Honorspren.createAttributes().build());
	}
}
