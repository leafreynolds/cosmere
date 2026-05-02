
/*
 * File updated ~ 6 - 2 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.properties.PropTypes;
import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.items.*;
import leaf.cosmere.surgebinding.common.items.tiers.ShardbladeItemTier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurgebindingItems
{
	private SurgebindingItems()
	{
	}

	public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Surgebinding.MODID);


	public static final ShardbladeItemTier SHARDBLADE_ITEM_TIER = new ShardbladeItemTier(10);

	public static final ItemRegistryObject<NightbloodItem> NIGHTBLOOD = ITEMS.register("nightblood", () -> new NightbloodItem(SHARDBLADE_ITEM_TIER, 24, -2.4F, PropTypes.Items.SHARDBLADE.get()));
	public static final ItemRegistryObject<ShardbladeItem> TEST_BLADE = ITEMS.register("test_blade", () -> new ShardbladeItem(SHARDBLADE_ITEM_TIER, 10, -2.4F, PropTypes.Items.SHARDBLADE.get()));
	public static final ItemRegistryObject<ShardbladeItem> SHARDBLADE = ITEMS.register("shardblade", () -> new ShardbladeDynamicItem(SHARDBLADE_ITEM_TIER, 24, -2.4F, PropTypes.Items.SHARDBLADE.get()));
	public static final ItemRegistryObject<ShardbladeItem> MASTER_SWORD = ITEMS.register("master_sword", () -> new ShardbladeItem(SHARDBLADE_ITEM_TIER, 10, -2.4F, PropTypes.Items.SHARDBLADE.get()));

	public static final ItemRegistryObject<ShardplateCurioItem> SHARDPLATE = ITEMS.register("shardplate", () -> new ShardplateCurioItem(PropTypes.Items.SHARDPLATE.get()));

	public static final ItemRegistryObject<BannerPatternItem> SURGE_BANNER_PATTERN = ITEMS.register("surge_banner_pattern", () -> new BannerPatternItem(SurgebindingTags.BannerPatterns.PATTERN_ITEM_SURGE, new Item.Properties().stacksTo(1)));
	public static final ItemRegistryObject<BannerPatternItem> RADIANT_ORDER_BANNER_PATTER = ITEMS.register("radiant_order_banner_pattern", () -> new BannerPatternItem(SurgebindingTags.BannerPatterns.PATTERN_ITEM_RADIANT_ORDER, new Item.Properties().stacksTo(1)));

	public static final Map<Roshar.RadiantOrder, ItemRegistryObject<HonorbladeItem>> HONORBLADES =
			Arrays.stream(EnumUtils.RADIANT_ORDERS)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_honorblade",
									() -> new HonorbladeItem(type, SHARDBLADE_ITEM_TIER, 24, -2.4F, PropTypes.Items.SHARDBLADE.get())
							)));

	public static final Map<Roshar.Gemstone, ItemRegistryObject<GemstoneItem>> GEMSTONE_SMALL =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register("cut_" + type.getName() + "_small",
									() -> new GemstoneItem(type, Roshar.GemSize.SMALL))
					));

	public static final Map<Roshar.Gemstone, ItemRegistryObject<GemstoneItem>> GEMSTONE_MEDIUM =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register("cut_" + type.getName() + "_medium",
									() -> new GemstoneItem(type, Roshar.GemSize.MEDIUM))
					));

	public static final Map<Roshar.Gemstone, ItemRegistryObject<GemstoneItem>> GEMSTONE_LARGE =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register("cut_" + type.getName() + "_large",
									() -> new GemstoneItem(type, Roshar.GemSize.LARGE))
					));

	static Roshar.Gemstone[] gemstone = {Roshar.Gemstone.SAPPHIRE, Roshar.Gemstone.SMOKESTONE, Roshar.Gemstone.RUBY, Roshar.Gemstone.DIAMOND, Roshar.Gemstone.GARNET, Roshar.Gemstone.ZIRCON, Roshar.Gemstone.TOPAZ, Roshar.Gemstone.HELIODOR};
	public static final Map<Roshar.Gemstone, ItemRegistryObject<Item>> GEMSTONE =
			Arrays.stream(gemstone)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type == Roshar.Gemstone.DIAMOND ? "rosharan_diamond" : type.getName(),
									() -> new Item(new Item.Properties()))
					));

	public static final Map<Roshar.Gemstone, ItemRegistryObject<Item>> GEMSTONE_MARKS =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + Constants.RegNameStubs.MARK,
									() -> new Item(new Item.Properties())
							)));

	public static final ItemRegistryObject<Item> RAW_CHULL_LEG = ITEMS.register("raw_chull_leg", () -> new Item(new Item.Properties()
			.food(new FoodProperties.Builder()
					.nutrition(5)
					.meat()
					.build())));

	public static final ItemRegistryObject<Item> COOKED_CHULL_LEG = ITEMS.register("cooked_chull_leg", () -> new Item(new Item.Properties()
			.food(new FoodProperties.Builder()
					.nutrition(10)
					.meat()
					.build())));

	public static final ItemRegistryObject<Item> RAW_CHULL_MEAT = ITEMS.register("raw_chull_meat", () -> new Item(new Item.Properties()
			.food(new FoodProperties.Builder()
					.nutrition(4)
					.meat()
					.build())));

	public static final ItemRegistryObject<Item> COOKED_CHULL_MEAT = ITEMS.register("cooked_chull_meat", () -> new Item(new Item.Properties()
			.food(new FoodProperties.Builder()
					.nutrition(8)
					.meat()
					.build())));


	public static final ItemRegistryObject<ForgeSpawnEggItem> CHULL_EGG = ITEMS.registerSpawnEgg(SurgebindingEntityTypes.CHULL, 0x6c482f, 0x8a1a08);
	public static final ItemRegistryObject<ForgeSpawnEggItem> CRYPTIC_EGG = ITEMS.registerSpawnEgg(SurgebindingEntityTypes.CRYPTIC, 0x272727, 0x4d4d4d);
}
