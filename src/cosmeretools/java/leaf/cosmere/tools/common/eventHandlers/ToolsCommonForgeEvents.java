/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.eventHandlers;

import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.commands.ToolsCommands;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CosmereTools.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ToolsCommonForgeEvents
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
			//if (stack.getItem() instanceof ToolsItem toolsItem)
			//{
			//
			//}
		}
	}


	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		ToolsCommands.register(event.getDispatcher());
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
