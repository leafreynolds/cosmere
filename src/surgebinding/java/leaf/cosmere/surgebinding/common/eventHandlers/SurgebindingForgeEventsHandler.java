package leaf.cosmere.surgebinding.common.eventHandlers;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.commands.SurgebindingCommands;
import leaf.cosmere.surgebinding.common.manifestation.SurgeGravitation;
import leaf.cosmere.surgebinding.common.manifestation.SurgeProgression;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Surgebinding.MODID, bus = EventBusSubscriber.Bus.GAME)
public class SurgebindingForgeEventsHandler
{
	@SubscribeEvent
	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
	{
		if (event.isCanceled())
		{
			return;
		}

		SurgeProgression.onBlockInteract(event);
	}

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		SurgebindingCommands.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity) || event.isCanceled())
		{
			return;
		}

		SurgeProgression.onEntityInteract(event);
	}

	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event)
	{
		SurgeGravitation.onLivingAttackEvent(event);
	}

	@SubscribeEvent
	public static void onServerChatEvent(ServerChatEvent event)
	{
		SpiritwebCapability.get(event.getPlayer()).ifPresent(spiritweb ->
		{
			final ISpiritwebSubmodule submodule = spiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
			if (submodule instanceof SurgebindingSpiritwebSubmodule surgebinding)
			{
				surgebinding.onChatMessageReceived(event);
			}
		});
	}
}
