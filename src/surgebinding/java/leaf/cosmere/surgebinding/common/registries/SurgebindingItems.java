
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

	public static final ItemRegistryObject<ShardplateCurioItem> SHARDPLATE = ITEMS.register("shardplate", () -> new ShardplateCurioItem(PropTypes.Items.SHARDPLATE.get()));


	public static final Map<Roshar.RadiantOrder, ItemRegistryObject<HonorbladeItem>> HONORBLADES =
			Arrays.stream(EnumUtils.RADIANT_ORDERS)
					.collect(Collectors.toMap(
							Function.identity(),
							type -> ITEMS.register(
									type.getName() + "_honorblade",
									() -> new HonorbladeItem(type, SHARDBLADE_ITEM_TIER, 24, -2.4F, PropTypes.Items.SHARDBLADE.get())
							)));


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
