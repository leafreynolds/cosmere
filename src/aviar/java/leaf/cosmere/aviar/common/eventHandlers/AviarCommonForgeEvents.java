/*
 * File updated ~ 20 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.aviar.common.eventHandlers;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.common.commands.AviarCommands;
import leaf.cosmere.aviar.common.registries.AviarItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

@EventBusSubscriber(modid = Aviar.MODID)
public class AviarCommonForgeEvents
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
			//if (stack.getItem() instanceof AviarItem aviarItem)
			//{
			//
			//}
		}
	}


	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		AviarCommands.register(event.getDispatcher());
	}


	@SubscribeEvent
	public static void onServerStartingEvent(ServerStartedEvent event)
	{

	}

	@SubscribeEvent
	public static void onServerStoppingEvent(ServerStoppingEvent event)
	{

	}

	@SubscribeEvent
	public static void registerTrades(WandererTradesEvent event)
	{
		final ItemStack price = new ItemStack(Items.EMERALD, 16);
		final ItemStack price2 = new ItemStack(Items.NETHER_STAR);
		final ItemStack forSale = new ItemStack(AviarItems.PATJIS_FRUIT);

		event.getGenericTrades().add(
				new BasicItemListing(
						price,
						price2,
						forSale,
						1,
						8,
						1
				)
		);
	}
}
