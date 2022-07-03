/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;


import leaf.cosmere.Cosmere;
import leaf.cosmere.blocks.MetalOreBlock;
import leaf.cosmere.commands.CosmereCommand;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.VillagerProfessionRegistry;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
						addBlendTrades(tradesForLevel, Rarity.COMMON);
					}
					break;
					case 3:
					{
						//addNuggetTrades(tradesForLevel, Rarity.UNCOMMON);
						addBlendTrades(tradesForLevel, Rarity.COMMON);

						ItemStack itemStackForSale = new ItemStack(ItemsRegistry.METAL_VIAL.get(), 1);
						tradesForLevel.add(makeTrade(itemStackForSale));
					}
					break;
					case 4:
					{
						//addNuggetTrades(tradesForLevel, Rarity.RARE);
						addRawOreTrades(tradesForLevel, Rarity.COMMON);
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
		for (RegistryObject<Item> item : ItemsRegistry.METAL_NUGGETS.values())
		{
			if (item.get().getRarity(ItemStack.EMPTY) == rarity)
			{
				ItemStack itemStackForSale = new ItemStack(item.get(), 1);
				tradesForLevel.add(makeTrade(itemStackForSale));
			}
		}
	}

	private static void addBlendTrades(List<VillagerTrades.ItemListing> tradesForLevel, Rarity rarity)
	{
		for (RegistryObject<Item> item : ItemsRegistry.METAL_RAW_BLEND.values())
		{
			if (item.get().getRarity(ItemStack.EMPTY) == rarity)
			{
				ItemStack itemStackForSale = new ItemStack(item.get(), 1);
				tradesForLevel.add(makeTrade(itemStackForSale));
			}
		}
	}

	private static void addRawOreTrades(List<VillagerTrades.ItemListing> tradesForLevel, Rarity rarity)
	{
		for (RegistryObject<Item> item : ItemsRegistry.METAL_RAW_ORE.values())
		{
			if (item.get().getRarity(ItemStack.EMPTY) == rarity)
			{
				ItemStack itemStackForSale = new ItemStack(item.get(), 1);
				tradesForLevel.add(makeTrade(itemStackForSale));
			}
		}
	}

	private static void addOreTrades(List<VillagerTrades.ItemListing> tradesForLevel, Rarity rarity)
	{
		for (RegistryObject<MetalOreBlock> item : BlocksRegistry.METAL_ORE.values())
		{
			if (item.get().asItem().getRarity(ItemStack.EMPTY) == rarity)
			{
				ItemStack itemStackForSale = new ItemStack(item.get().asItem(), 1);
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
		switch (item.getItem().getRarity(item))
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
		switch (item.getItem().getRarity(item))
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
		switch (item.getItem().getRarity(item))
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
		switch (item.getItem().getRarity(item))
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
}