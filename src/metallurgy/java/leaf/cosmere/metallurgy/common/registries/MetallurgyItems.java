package leaf.cosmere.metallurgy.common.registries;

import leaf.cosmere.common.registration.impl.ItemDeferredRegister;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.items.AlloyIngotItem;
import leaf.cosmere.metallurgy.common.items.AlloyFragmentItem;
import leaf.cosmere.metallurgy.common.items.AlloyPowderItem;
import leaf.cosmere.metallurgy.common.items.MetallurgistChiselItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

public class MetallurgyItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(Metallurgy.MODID);

    public static final ItemRegistryObject<MetallurgistChiselItem> IRON_CHISEL = ITEMS.register(
            "iron_chisel",
            () -> new MetallurgistChiselItem(Tiers.IRON, 1.0F, -2.8F, new Item.Properties()));

    public static final ItemRegistryObject<MetallurgistChiselItem> DIAMOND_CHISEL = ITEMS.register(
            "diamond_chisel",
            () -> new MetallurgistChiselItem(Tiers.DIAMOND, 1.0F, -2.8F, new Item.Properties()));

    public static final ItemRegistryObject<MetallurgistChiselItem> NETHERITE_CHISEL = ITEMS.register(
            "netherite_chisel",
            () -> new MetallurgistChiselItem(Tiers.NETHERITE, 1.0F, -2.8F, new Item.Properties().fireResistant()));

    public static final ItemRegistryObject<AlloyPowderItem> ALLOY_POWDER = ITEMS.register(
            "alloy_powder",
            () -> new AlloyPowderItem(new Item.Properties()));

    public static final ItemRegistryObject<AlloyFragmentItem> METAL_FRAGMENT = ITEMS.register(
            "alloy_fragment",
            () -> new AlloyFragmentItem(new Item.Properties()));

    public static final ItemRegistryObject<AlloyIngotItem> ALLOY_INGOT = ITEMS.register(
            "alloy_ingot",
            () -> new AlloyIngotItem(new Item.Properties()));
}
