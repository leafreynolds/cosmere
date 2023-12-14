/*
 * File updated ~ 30 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aondor.common.eventHandlers;

import leaf.cosmere.aondor.common.AonDor;
import leaf.cosmere.aondor.common.commands.AonDorCommands;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AonDor.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AonDorCommonForgeEvents
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
			//if (stack.getItem() instanceof AonDorItem aondorItem)
			//{
			//
			//}
		}
	}


	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		AonDorCommands.register(event.getDispatcher());
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
