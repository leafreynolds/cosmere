/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;


import leaf.cosmere.Cosmere;
import leaf.cosmere.blocks.MetalOreBlock;
import leaf.cosmere.commands.CosmereCommand;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.FeatureRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.VillagerProfessionRegistry;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents
{
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event)
	{
		CosmereCommand.register(event.getDispatcher());
	}


	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onBiomeLoad(BiomeLoadingEvent event)
	{
		Biome.BiomeCategory biomeCategory = event.getCategory();

		//whitelist only vanilla biomes.
		//todo may have compatibility issues with mining dimension type mods
		//don't really want to deprive people of easier access to ores if they want to play that way. something to look into.
		List<Biome.BiomeCategory> whitelist =
				Arrays.stream(Biome.BiomeCategory.values())
						.filter(biomeType -> biomeType != Biome.BiomeCategory.NONE && biomeType != Biome.BiomeCategory.NETHER && biomeType != Biome.BiomeCategory.THEEND)
						.collect(Collectors.toList());


		if (whitelist.contains(biomeCategory))
		{
			for (Metals.MetalType metalType : Metals.MetalType.values())
			{
				if (metalType.hasOre())
				{
					event.getGeneration().addFeature(
							GenerationStep.Decoration.UNDERGROUND_ORES,
							FeatureRegistry.PlacedFeatures.PLACED_ORE_FEATURES.get(metalType)
					);

					//LogHelper.debug(String.format("Added %s to: %s", metalType.getName(), event.getName()));
				}
			}

		}
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
						for (RegistryObject<Item> item : ItemsRegistry.METAL_NUGGETS.values())
						{
							tradesForLevel.add(makeTrade(item.get()));
						}
						break;
					case 2:
						for (RegistryObject<Item> item : ItemsRegistry.METAL_RAW_BLEND.values())
						{
							tradesForLevel.add(makeTrade(item.get()));
						}
						break;
					case 3:
						tradesForLevel.add(makeTrade(ItemsRegistry.METAL_VIAL.get()));
						break;
					case 4:
						for (RegistryObject<Item> item : ItemsRegistry.METAL_RAW_ORE.values())
						{
							tradesForLevel.add(makeTrade(item.get()));
						}
						break;
					case 5:
						for (RegistryObject<MetalOreBlock> item : BlocksRegistry.METAL_ORE.values())
						{
							tradesForLevel.add(makeTrade(item.get().asItem()));
						}
						break;
				}
			}
		}
	}


	private static VillagerTrades.ItemListing makeTrade(Item item)
	{
		ItemStack itemStackForSale = new ItemStack(item, 1);

		itemStackForSale.setCount(getCount(itemStackForSale));

		return new BasicItemListing(
				getCost(itemStackForSale),
				itemStackForSale,
				getMaxTradesPerDay(itemStackForSale),
				getXpPerTrade(itemStackForSale));

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