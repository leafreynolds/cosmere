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
import leaf.cosmere.surgebinding.common.items.tiers.ShardplateArmorMaterial;
import net.minecraft.world.item.ArmorItem;
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

	public static final ItemRegistryObject<ShardbladeItem> NIGHTBLOOD = ITEMS.register("nightblood", () -> new NightbloodItem(SHARDBLADE_ITEM_TIER, 24, -2.4F, PropTypes.Items.SHARDBLADE.get()));
	public static final ItemRegistryObject<ShardbladeItem> TEST_BLADE = ITEMS.register("test_blade", () -> new ShardbladeItem(SHARDBLADE_ITEM_TIER, 10, -2.4F, PropTypes.Items.SHARDBLADE.get()));
	public static final ItemRegistryObject<ShardbladeItem> SHARDBLADE = ITEMS.register("shardblade", () -> new ShardbladeDynamicItem(SHARDBLADE_ITEM_TIER, 24, -2.4F, PropTypes.Items.SHARDBLADE.get()));
	public static final ItemRegistryObject<ShardbladeItem> MASTER_SWORD = ITEMS.register("master_sword", () -> new ShardbladeItem(SHARDBLADE_ITEM_TIER, 10, -2.4F, PropTypes.Items.SHARDBLADE.get()));

	public static final ItemRegistryObject<ShardplateItem> SHARDPLATE_HELMET = ITEMS.register("shardplate_helmet", () -> new ShardplateItem(ShardplateArmorMaterial.DEADPLATE, ArmorItem.Type.HELMET, PropTypes.Items.SHARDBLADE.get()));
	public static final ItemRegistryObject<ShardplateItem> SHARDPLATE_CHEST = ITEMS.register("shardplate_chest", () -> new ShardplateItem(ShardplateArmorMaterial.DEADPLATE, ArmorItem.Type.CHESTPLATE, PropTypes.Items.SHARDBLADE.get()));
	public static final ItemRegistryObject<ShardplateItem> SHARDPLATE_LEGGINGS = ITEMS.register("shardplate_leggings", () -> new ShardplateItem(ShardplateArmorMaterial.DEADPLATE, ArmorItem.Type.LEGGINGS, PropTypes.Items.SHARDBLADE.get()));
	public static final ItemRegistryObject<ShardplateItem> SHARDPLATE_BOOTS = ITEMS.register("shardplate_boots", () -> new ShardplateItem(ShardplateArmorMaterial.DEADPLATE, ArmorItem.Type.BOOTS, PropTypes.Items.SHARDBLADE.get()));

	public static final ItemRegistryObject<BannerPatternItem> SURGE_BANNER_PATTERN = ITEMS.register("surge_banner_pattern",()-> new BannerPatternItem(SurgebindingTags.BannerPatterns.PATTERN_ITEM_SURGE, new Item.Properties().stacksTo(1)));

	public static final Map<Roshar.RadiantOrder, ItemRegistryObject<HonorbladeItem>> HONORBLADES =
			Arrays.stream(EnumUtils.RADIANT_ORDERS)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_honorblade",
									() -> new HonorbladeItem(type, SHARDBLADE_ITEM_TIER, 24, -2.4F, PropTypes.Items.SHARDBLADE.get())
							)));

	public static final Map<Roshar.Gemstone, ItemRegistryObject<Item>> GEMSTONE_SMALL =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register("cut_"+type.getName()+"_small",
									()-> new Item(new Item.Properties()))
							));

	public static final Map<Roshar.Gemstone, ItemRegistryObject<Item>> GEMSTONE_MEDIUM =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register("cut_"+type.getName()+"_medium",
									()-> new Item(new Item.Properties()))
							));

	public static final Map<Roshar.Gemstone, ItemRegistryObject<Item>> GEMSTONE_LARGE =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register("cut_"+type.getName()+"_large",
									()-> new Item(new Item.Properties()))
							));

	public static final ItemRegistryObject<Item> ROSHARAN_DIAMOND = ITEMS.register("rosharan_diamond",
			() -> new Item(new Item.Properties()));
	public static final ItemRegistryObject<Item> GARNET = ITEMS.register("garnet",
			() -> new Item(new Item.Properties()));
	public static final ItemRegistryObject<Item> HELIODOR = ITEMS.register("heliodor",
			() -> new Item(new Item.Properties()));
	public static final ItemRegistryObject<Item> RUBY = ITEMS.register("ruby",
			() -> new Item(new Item.Properties()));
	public static final ItemRegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire",
			() -> new Item(new Item.Properties()));
	public static final ItemRegistryObject<Item> SMOKESTONE = ITEMS.register("smokestone",
			() -> new Item(new Item.Properties()));
	public static final ItemRegistryObject<Item> TOPAZ = ITEMS.register("topaz",
			() -> new Item(new Item.Properties()));
	public static final ItemRegistryObject<Item> zircon = ITEMS.register("zircon",
			() -> new Item(new Item.Properties()));

	public static final Map<Roshar.Gemstone, ItemRegistryObject<GemstoneItem>> GEMSTONE_CHIPS =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + Constants.RegNameStubs.CHIP,
									() -> new GemstoneItem(type, Roshar.GemSize.CHIP)
							)));


	public static final Map<Roshar.Gemstone, ItemRegistryObject<GemstoneItem>> GEMSTONE_MARKS =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + Constants.RegNameStubs.MARK,
									() -> new GemstoneItem(type, Roshar.GemSize.MARK)
							)));


	public static final Map<Roshar.Gemstone, ItemRegistryObject<GemstoneItem>> GEMSTONE_BROAMS =
			Arrays.stream(EnumUtils.GEMSTONE_TYPES)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + Constants.RegNameStubs.BROAM,
									() -> new GemstoneItem(type, Roshar.GemSize.BROAM)
							)));

	public static final ItemRegistryObject<ForgeSpawnEggItem> CHULL_EGG = ITEMS.registerSpawnEgg(SurgebindingEntityTypes.CHULL, 0x6c482f, 0x8a1a08);
	public static final ItemRegistryObject<ForgeSpawnEggItem> CRYPTIC_EGG = ITEMS.registerSpawnEgg(SurgebindingEntityTypes.CRYPTIC, 0x272727, 0x4d4d4d);
}
