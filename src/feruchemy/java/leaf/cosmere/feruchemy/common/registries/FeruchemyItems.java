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

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeruchemyItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Feruchemy.MODID);


	public static final ItemRegistryObject<BandsOfMourningItem> BANDS_OF_MOURNING = ITEMS.register("bands_of_mourning", BandsOfMourningItem::new);

	public static final Map<Metals.MetalType, ItemRegistryObject<RingMetalmindItem>> METAL_RINGS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.filter(metalType -> metalType != Metals.MetalType.NICROSIL)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.RING + RegNameStubs.METALMIND,
									() -> new RingMetalmindItem(type)
							)));

	public static final Map<Metals.MetalType, ItemRegistryObject<BraceletMetalmindItem>> METAL_BRACELETS =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.filter(metalType -> metalType != Metals.MetalType.NICROSIL)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.BRACELET + RegNameStubs.METALMIND,
									() -> new BraceletMetalmindItem(type)
							)));

	public static final Map<Metals.MetalType, ItemRegistryObject<NecklaceMetalmindItem>> METAL_NECKLACES =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.filter(Metals.MetalType::hasFeruchemicalEffect)
					.filter(metalType -> metalType != Metals.MetalType.NICROSIL)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.NECKLACE + RegNameStubs.METALMIND,
									() -> new NecklaceMetalmindItem(type)
							)));

	public static final ItemRegistryObject<NicrosilRingMetalmindItem> NICROSIL_METAL_RING =
			ITEMS.register(Metals.MetalType.NICROSIL.getName() + RegNameStubs.RING + RegNameStubs.METALMIND,
									() -> new NicrosilRingMetalmindItem(Metals.MetalType.NICROSIL));

	public static final ItemRegistryObject<NicrosilBraceletMetalmindItem> NICROSIL_METAL_BRACELET =
			ITEMS.register(Metals.MetalType.NICROSIL.getName() + RegNameStubs.BRACELET + RegNameStubs.METALMIND,
					() -> new NicrosilBraceletMetalmindItem(Metals.MetalType.NICROSIL));

	public static final ItemRegistryObject<NicrosilNecklaceMetalmindItem> NICROSIL_METAL_NECKLACE =
			ITEMS.register(Metals.MetalType.NICROSIL.getName() + RegNameStubs.NECKLACE + RegNameStubs.METALMIND,
					() -> new NicrosilNecklaceMetalmindItem(Metals.MetalType.NICROSIL));

}
