/*
 * File updated ~ 21 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.eventHandlers;

import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.common.commands.AviarCommands;
import leaf.cosmere.aviar.common.registries.AviarItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Aviar.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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

		event.getRareTrades().add(
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
