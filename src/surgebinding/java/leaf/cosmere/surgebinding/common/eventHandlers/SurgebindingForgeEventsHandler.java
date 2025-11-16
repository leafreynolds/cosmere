/*
 * File updated ~ 4 - 2 - 2025 ~ Leaf
 */

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
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Surgebinding.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SurgebindingForgeEventsHandler
{
/*
	//todo fix roshar not respecting sleep ??
	@SubscribeEvent
	public static void onSleepFinished(final SleepFinishedTimeEvent evt)
	{
		LevelAccessor levelAccessor = evt.getLevel();

		if (levelAccessor instanceof ServerLevel serverLevel)
		{
			long newTime = evt.getNewTime();

			ServerLevel level = serverLevel.getServer().getLevel(SurgebindingDimensions.ROSHAR_DIM_KEY);
			if (level != null)
			{
				evt.setTimeAddition(600);
			}
		}
	}*/

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
		if (!(event.getTarget() instanceof LivingEntity target) || event.isCanceled())
		{
			return;
		}

		SurgeProgression.onEntityInteract(event);
	}


	//Attack event happens first
	@SubscribeEvent
	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		if (event.isCanceled())
		{
			return;
		}

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
