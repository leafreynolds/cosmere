/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.cosmere.registry;

import leaf.cosmere.Cosmere;
import leaf.cosmere.blocks.*;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlocksRegistry
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Cosmere.MODID);

	public static final RegistryObject<Block> METALWORKING_TABLE = register("metalworking_table", () -> (new MetalworkingTableBlock()), Rarity.COMMON);

	public static final Map<Metals.MetalType, RegistryObject<MetalBlock>> METAL_BLOCKS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType -> register(
									metalType.getName() + Constants.RegNameStubs.BLOCK,
									() -> new MetalBlock(metalType), metalType.getRarity())));

	public static final Map<Roshar.Gemstone, RegistryObject<GemBlock>> GEM_BLOCKS =
			Arrays.stream(Roshar.Gemstone.values())
					.collect(Collectors.toMap(
							Function.identity(),
							gemstone -> register(
									gemstone.getName() + Constants.RegNameStubs.BLOCK,
									() -> new GemBlock(gemstone), Rarity.UNCOMMON)));

	public static final Map<Metals.MetalType, RegistryObject<MetalOreBlock>> METAL_ORE =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasOre)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType -> register(
									metalType.getName() + Constants.RegNameStubs.ORE,
									() -> new MetalOreBlock(metalType),
									metalType.getRarity())));

	public static final Map<Metals.MetalType, RegistryObject<MetalOreBlock>> METAL_ORE_DEEPSLATE =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasOre)
					.collect(Collectors.toMap(
							Function.identity(),
							metalType -> register(
									Constants.RegNameStubs.DEEPSLATE + metalType.getName() + Constants.RegNameStubs.ORE,
									() -> new MetalOreBlock(metalType),
									metalType.getRarity())));

	public static final Map<Roshar.Gemstone, RegistryObject<GemOreBlock>> GEM_ORE =
			Arrays.stream(Roshar.Gemstone.values())
					.collect(Collectors.toMap(
							Function.identity(),
							gemstone -> register(
									gemstone.getName() + Constants.RegNameStubs.ORE,
									() -> new GemOreBlock(gemstone),
									Rarity.UNCOMMON)));

	public static final Map<Roshar.Gemstone, RegistryObject<GemOreBlock>> GEM_ORE_DEEPSLATE =
			Arrays.stream(Roshar.Gemstone.values())
					.collect(Collectors.toMap(
							Function.identity(),
							gemstone -> register(
									Constants.RegNameStubs.DEEPSLATE + gemstone.getName() + Constants.RegNameStubs.ORE,
									() -> new GemOreBlock(gemstone),
									Rarity.UNCOMMON)));


	private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, Rarity itemRarity)
	{
		RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
		ItemsRegistry.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().tab(CosmereItemGroups.BLOCKS).rarity(itemRarity)));


		return registryObject;
	}

}
