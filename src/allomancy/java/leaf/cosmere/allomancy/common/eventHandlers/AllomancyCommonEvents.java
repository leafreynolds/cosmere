/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.eventHandlers;


import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.common.registry.VillagerProfessionRegistry;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Allomancy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AllomancyCommonEvents
{
	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event)
	{
		if (event.getType() == VillagerProfessionRegistry.METAL_TRADER.get())
		{
			final List<VillagerTrades.ItemListing> tradesForLevel = event.getTrades().get(3);
			ItemStack itemStackForSale = new ItemStack(AllomancyItems.METAL_VIAL.get());
			tradesForLevel.add(new BasicItemListing(
					3,
					itemStackForSale,
					8,
					2));
		}
	}
}