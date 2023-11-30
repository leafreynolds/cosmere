/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.soulforgery.common.eventHandlers;

import leaf.cosmere.soulforgery.common.Soulforgery;
import leaf.cosmere.soulforgery.common.commands.SoulforgeryCommands;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Soulforgery.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
