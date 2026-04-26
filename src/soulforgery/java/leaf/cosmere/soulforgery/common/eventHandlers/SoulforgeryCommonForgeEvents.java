/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.soulforgery.common.eventHandlers;

import leaf.cosmere.soulforgery.common.Soulforgery;
import leaf.cosmere.soulforgery.common.commands.SoulforgeryCommands;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Soulforgery.MODID, bus = Bus.GAME)
public class SoulforgeryCommonForgeEvents
{


	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity target))
		{
			return;
		}

		ItemStack stack = event.getEntity().getMainHandItem();
		if (!stack.isEmpty())
		{
			//if (stack.getItem() instanceof SoulforgeryItem soulforgeryItem)
			//{
			//
			//}
		}
	}


	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		SoulforgeryCommands.register(event.getDispatcher());
	}


	@SubscribeEvent
	public static void onServerStartingEvent(ServerStartedEvent event)
	{

	}

	@SubscribeEvent
	public static void onServerStoppingEvent(ServerStoppingEvent event)
	{

	}

}
