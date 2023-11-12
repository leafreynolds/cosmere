/*
 * File updated ~ 15 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.registries;

import leaf.cosmere.api.Constants.RegNameStubs;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;

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

	public static final ItemRegistryObject<SwordItem> KOLOSS_SWORD = ITEMS.register("koloss_sword", () -> new SwordItem(Tiers.IRON, 10, -2.4F, PropTypes.Items.ONE.get()));

	public static final ItemRegistryObject<ForgeSpawnEggItem> SMALL_KOLOSS_EGG = ITEMS.registerSpawnEgg(HemalurgyEntityTypes.KOLOSS_SMALL, 0x314f64, 0x413222);
	public static final ItemRegistryObject<ForgeSpawnEggItem> MEDIUM_KOLOSS_EGG = ITEMS.registerSpawnEgg(HemalurgyEntityTypes.KOLOSS_MEDIUM, 0x314f64, 0x401815);
	public static final ItemRegistryObject<ForgeSpawnEggItem> LARGE_KOLOSS_EGG = ITEMS.registerSpawnEgg(HemalurgyEntityTypes.KOLOSS_LARGE, 0x314f64, 0x5a0b0b);
}
