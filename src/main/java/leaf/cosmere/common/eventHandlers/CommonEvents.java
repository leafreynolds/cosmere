/*
 * File updated ~ 9 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.common.eventHandlers;


import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.commands.CosmereCommand;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import leaf.cosmere.common.registry.VillagerProfessionRegistry;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

@EventBusSubscriber(modid = Cosmere.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEvents
{
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		CosmereCommand.register(event.getDispatcher());
	}


	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event)
	{
		if (event.getType() == VillagerProfessionRegistry.METAL_TRADER.get())
		{
			for (int i = 1; i <= 5; i++)
			{
				final List<VillagerTrades.ItemListing> tradesForLevel = event.getTrades().get(i);
				switch (i)
				{
					case 1:
					{
						addNuggetTrades(tradesForLevel, Rarity.COMMON);
					}
					break;
					case 2:
					{
						addRawOreTrades(tradesForLevel, Rarity.COMMON);
					}
					break;
					case 3:
					case 4:
					{
						addBlendTrades(tradesForLevel, Rarity.COMMON);
					}
					break;
					case 5:
					{
						//addNuggetTrades(tradesForLevel, Rarity.EPIC);
						addOreTrades(tradesForLevel, Rarity.COMMON);
					}
					break;
				}
			}
		}
	}

	private static void addNuggetTrades(List<VillagerTrades.ItemListing> tradesForLevel, Rarity rarity)
	{
		for (ItemRegistryObject<Item> item : ItemsRegistry.METAL_NUGGETS.values())
		{
			ItemStack itemStackForSale = new ItemStack(item.get(), 1);
			if (itemStackForSale.getRarity() == rarity)
			{
				tradesForLevel.add(makeTrade(itemStackForSale));
			}
		}
	}

	private static void addBlendTrades(List<VillagerTrades.ItemListing> tradesForLevel, Rarity rarity)
	{
		for (ItemRegistryObject<Item> item : ItemsRegistry.METAL_RAW_BLEND.values())
		{
			ItemStack itemStackForSale = new ItemStack(item.get(), 1);
			if (itemStackForSale.getRarity() == rarity)
			{
				tradesForLevel.add(makeTrade(itemStackForSale));
			}
		}
	}

	private static void addRawOreTrades(List<VillagerTrades.ItemListing> tradesForLevel, Rarity rarity)
	{
		for (ItemRegistryObject<Item> item : ItemsRegistry.METAL_RAW_ORE.values())
		{
			ItemStack itemStackForSale = new ItemStack(item.get(), 1);
			if (itemStackForSale.getRarity() == rarity)
			{
				tradesForLevel.add(makeTrade(itemStackForSale));
			}
		}
	}

	private static void addOreTrades(List<VillagerTrades.ItemListing> tradesForLevel, Rarity rarity)
	{
		for (var oreType : BlocksRegistry.METAL_ORE.values())
		{
			ItemStack itemStackForSale = new ItemStack(oreType.stone().getBlock().asItem(), 1);
			if (itemStackForSale.getRarity() == rarity)
			{
				tradesForLevel.add(makeTrade(itemStackForSale));
			}
		}
	}


	private static VillagerTrades.ItemListing makeTrade(ItemStack itemStack)
	{

		itemStack.setCount(getCount(itemStack));

		return new BasicItemListing(
				getCost(itemStack),
				itemStack,
				getMaxTradesPerDay(itemStack),
				getXpPerTrade(itemStack));

	}

	private static int getCost(ItemStack item)
	{
		int cost = 0;
		switch (item.getRarity())
		{
			case COMMON:
				cost = 1;
				break;
			case UNCOMMON:
				cost = 16;
				break;
			case RARE:
				cost = 32;
				break;
			case EPIC:
				cost = 64;
				break;
		}

		return cost;
	}

	private static int getCount(ItemStack item)
	{
		int count = 0;
		switch (item.getRarity())
		{
			case COMMON:
				count = 16;
				break;
			case UNCOMMON:
				count = 8;
				break;
			case RARE:
				count = 4;
				break;
			case EPIC:
				count = 1;
				break;
		}

		return count;
	}

	private static int getMaxTradesPerDay(ItemStack item)
	{
		int count = 0;
		switch (item.getRarity())
		{
			case COMMON:
				count = 8;
				break;
			case UNCOMMON:
				count = 5;
				break;
			case RARE:
				count = 3;
				break;
			case EPIC:
				count = 1;
				break;
		}

		return count;
	}

	private static int getXpPerTrade(ItemStack item)
	{
		int count = 0;
		switch (item.getRarity())
		{
			case COMMON:
				count = 2;
				break;
			case UNCOMMON:
				count = 4;
				break;
			case RARE:
				count = 6;
				break;
			case EPIC:
				count = 8;
				break;
		}

		return count;
	}


	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		//event.addListener(new RecipeReloadListener(event.getServerResources()));
	}
}