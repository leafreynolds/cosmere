/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import leaf.cosmere.api.Constants.RegNameStubs;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.items.*;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeruchemyItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Feruchemy.MODID);


	public static final ItemRegistryObject<BandsOfMourningItem> BANDS_OF_MOURNING = ITEMS.register("bands_of_mourning", BandsOfMourningItem::new);

	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_RINGS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.RING + RegNameStubs.METALMIND,
									() -> {
										if(type == Metals.MetalType.NICROSIL)
										{
											return new NicrosilRingMetalmindItem(type);
										}
										else
										{
											return new RingMetalmindItem(type);
										}
									})
					));

	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_BRACELETS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.BRACELET + RegNameStubs.METALMIND,
									() -> {
										if(type == Metals.MetalType.NICROSIL)
										{
											return new NicrosilBraceletMetalmindItem(type);
										}
										else
										{
											return new BraceletMetalmindItem(type);
										}
									})
							));

	public static final Map<Metals.MetalType, ItemRegistryObject<Item>> METAL_NECKLACES =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.NECKLACE + RegNameStubs.METALMIND,
									() -> {
										if(type == Metals.MetalType.NICROSIL)
										{
											return new NicrosilNecklaceMetalmindItem(type);
										}
										else
										{
											return new NecklaceMetalmindItem(type);
										}
									})
							));

}
