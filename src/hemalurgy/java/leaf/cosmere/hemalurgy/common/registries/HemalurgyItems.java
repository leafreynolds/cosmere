/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.registries;

import leaf.cosmere.api.Constants.RegNameStubs;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HemalurgyItems
{
	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Hemalurgy.MODID);

	//public static final ItemRegistryObject<Item> JAR_OF_BLOOD = ITEMS.register("jar_of_blood", () -> (new BaseItem(PropTypes.Items.ONE.get().tab(CosmereItemGroups.ITEMS))));

	public static final Map<Metals.MetalType, ItemRegistryObject<HemalurgicSpikeItem>> METAL_SPIKE =
			Arrays.stream(Metals.MetalType.values())
					.filter(Metals.MetalType::hasHemalurgicEffect)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + RegNameStubs.SPIKE,
									() -> new HemalurgicSpikeItem(type)
							)));


}
