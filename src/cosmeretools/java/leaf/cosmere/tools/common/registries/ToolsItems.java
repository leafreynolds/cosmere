/*
 * File updated ~ 3 - 4 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.common.registries;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.items.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToolsItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(CosmereTools.MODID);


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_PICKAXES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_pickaxe",
									() -> new TPickaxeItem(
											type,
											1,
											-2.8F,
											ToolsItemGroups.TOOL.get().rarity(type.getRarity()).tab(ToolsItemGroups.TOOLS_TAB)
									)
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_AXES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_axe",
									() -> new TAxeItem(
											type,
											5,
											-3.1F,
											ToolsItemGroups.TOOL.get().rarity(type.getRarity()).tab(ToolsItemGroups.TOOLS_TAB)
									)
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_SHOVEL =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_shovel",
									() -> new TShovelItem(
											type,
											1.5f,
											-3.0F,
											ToolsItemGroups.TOOL.get().rarity(type.getRarity()).tab(ToolsItemGroups.TOOLS_TAB)
									)
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_HOE =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_hoe",
									() -> new THoeItem(
											type,
											0,
											-3.0F,
											ToolsItemGroups.TOOL.get().rarity(type.getRarity()).tab(ToolsItemGroups.TOOLS_TAB)
									)
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_SWORDS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_sword",
									() -> new TSwordItem(
											type,
											3,
											-2.4F,
											ToolsItemGroups.TOOL.get().rarity(type.getRarity()).tab(ToolsItemGroups.TOOLS_TAB)
									)
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_HELMETS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_helmet",
									() -> new TArmorItem(type, EquipmentSlot.HEAD, (new Item.Properties()).rarity(type.getRarity()).tab(ToolsItemGroups.TOOLS_TAB))
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_CHESTPLATES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_chestplate",
									() -> new TArmorItem(type, EquipmentSlot.CHEST, (new Item.Properties()).rarity(type.getRarity()).tab(ToolsItemGroups.TOOLS_TAB))
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_LEGGINGS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_leggings",
									() -> new TArmorItem(type, EquipmentSlot.LEGS, (new Item.Properties()).rarity(type.getRarity()).tab(ToolsItemGroups.TOOLS_TAB))
							)));


	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_BOOTS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasMaterialItem)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_boots",
									() -> new TArmorItem(type, EquipmentSlot.FEET, (new Item.Properties()).rarity(type.getRarity()).tab(ToolsItemGroups.TOOLS_TAB))
							)));


}
