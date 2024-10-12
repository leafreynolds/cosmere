/*
 * File updated ~ 11 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.registries;

import leaf.cosmere.api.Constants.RegNameStubs;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.items.BandsOfMourningItem;
import leaf.cosmere.feruchemy.common.items.BraceletMetalmindItem;
import leaf.cosmere.feruchemy.common.items.NecklaceMetalmindItem;
import leaf.cosmere.feruchemy.common.items.RingMetalmindItem;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeruchemyItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Feruchemy.MODID);


	public static final ItemRegistryObject<BandsOfMourningItem> BANDS_OF_MOURNING = ITEMS.register("bands_of_mourning", BandsOfMourningItem::new);

	public static final Map<Metals.MetalType, ItemRegistryObject<RingMetalmindItem>> METAL_RINGS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.RING + RegNameStubs.METALMIND,
									() -> new RingMetalmindItem(type)
							)));

	public static final Map<Metals.MetalType, ItemRegistryObject<BraceletMetalmindItem>> METAL_BRACELETS =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.BRACELET + RegNameStubs.METALMIND,
									() -> new BraceletMetalmindItem(type)
							)));

	public static final Map<Metals.MetalType, ItemRegistryObject<NecklaceMetalmindItem>> METAL_NECKLACES =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.NECKLACE + RegNameStubs.METALMIND,
									() -> new NecklaceMetalmindItem(type)
							)));

}
