/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.registries;

import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.items.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ToolsItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(CosmereTools.MODID);
	public static final Supplier<Item.Properties> TOOL = () -> new Item.Properties();

	//enum hash codes change every launch, a plain toMap made registration and datagen order random
	private static <V> Collector<Metals.MetalType, ?, Map<Metals.MetalType, V>> toEnumMap(
			Function<Metals.MetalType, Metals.MetalType> key,
			Function<Metals.MetalType, V> value)
	{
		return Collectors.toMap(key, value, (a, b) -> a, () -> new EnumMap<>(Metals.MetalType.class));
	}

	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_PICKAXES =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(toEnumMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_pickaxe",
									() -> new TPickaxeItem(
											type,
											1,
											-2.8F,
											TOOL.get().rarity(type.getRarity())
									)
							)));

	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_AXES =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(toEnumMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_axe",
									() -> new TAxeItem(
											type,
											5,
											-3.1F,
											TOOL.get().rarity(type.getRarity())
									)
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_SHOVEL =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(toEnumMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_shovel",
									() -> new TShovelItem(
											type,
											1.5f,
											-3.0F,
											TOOL.get().rarity(type.getRarity())
									)
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_HOE =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(toEnumMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_hoe",
									() -> new THoeItem(
											type,
											0,
											-3.0F,
											TOOL.get().rarity(type.getRarity())
									)
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_SWORDS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(toEnumMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_sword",
									() -> new TSwordItem(
											type,
											3,
											-2.4F,
											TOOL.get().rarity(type.getRarity())
									)
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_HELMETS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(toEnumMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_helmet",
									() -> new TArmorItem(type, ArmorItem.Type.HELMET, (new Item.Properties()).rarity(type.getRarity())
									))));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_CHESTPLATES =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(toEnumMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_chestplate",
									() -> new TArmorItem(type, ArmorItem.Type.CHESTPLATE, (new Item.Properties()).rarity(type.getRarity())
									))));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_LEGGINGS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(toEnumMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_leggings",
									() -> new TArmorItem(type, ArmorItem.Type.LEGGINGS, (new Item.Properties()).rarity(type.getRarity())
									))));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_BOOTS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(toEnumMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_boots",
									() -> new TArmorItem(type, ArmorItem.Type.BOOTS, (new Item.Properties()).rarity(type.getRarity())
									))));


}
