package leaf.cosmere.metallurgy.common.registries;

import leaf.cosmere.common.registration.impl.CreativeTabDeferredRegister;
import leaf.cosmere.common.registration.impl.CreativeTabRegistryObject;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.util.AlloyComposition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import java.util.HashMap;
import java.util.Map;

public class MetallurgyCreativeTabs {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(Metallurgy.MODID,
            MetallurgyCreativeTabs::addToExistingTabs);

    public static final CreativeTabRegistryObject ITEMS = CREATIVE_TABS.registerMain(
            Component.translatable("tabs." + Metallurgy.MODID + ".items"),
            MetallurgyItems.ALLOY_INGOT,
            builder -> builder.withSearchBar()
                    .displayItems((displayParameters, output) -> {
                        // Add all items
                        CreativeTabDeferredRegister.addToDisplay(MetallurgyItems.ITEMS, output);

                        // Add example alloys with composition data
                        addExampleAlloys(output);
                    }));

    /**
     * Adds example alloy items with pre-configured composition data for testing.
     */
    private static void addExampleAlloys(CreativeModeTab.Output output) {
        // Example 1: Bronze (96% Copper, 4% Tin)
        ItemStack bronze = new ItemStack(MetallurgyItems.ALLOY_INGOT.get());
        Map<String, Double> bronzeComp = new HashMap<>();
        bronzeComp.put("minecraft:copper_ingot", 0.96);
        bronzeComp.put("cosmere:tin_ingot", 0.04);
        AlloyComposition.setComposition(bronze, bronzeComp);
        AlloyComposition.setContamination(bronze, 0.0);
        output.accept(bronze);

        // Example 2: Steel (98% Iron, 2% Carbon) with contamination
        ItemStack steel = new ItemStack(MetallurgyItems.ALLOY_INGOT.get());
        Map<String, Double> steelComp = new HashMap<>();
        steelComp.put("minecraft:iron_ingot", 0.98);
        steelComp.put("minecraft:coal", 0.02);
        AlloyComposition.setComposition(steel, steelComp);
        AlloyComposition.setContamination(steel, 0.05); // 5% contamination
        output.accept(steel);

        // Example 3: Pure Iron (for comparison)
        ItemStack pureIron = new ItemStack(MetallurgyItems.ALLOY_INGOT.get());
        Map<String, Double> ironComp = new HashMap<>();
        ironComp.put("minecraft:iron_ingot", 1.0);
        AlloyComposition.setComposition(pureIron, ironComp);
        AlloyComposition.setContamination(pureIron, 0.0);
        output.accept(pureIron);

        // Example 4: Metal Powder - Pure Copper
        ItemStack copperPowder = new ItemStack(MetallurgyItems.ALLOY_POWDER.get());
        Map<String, Double> copperComp = new HashMap<>();
        copperComp.put("minecraft:copper_ingot", 1.0);
        AlloyComposition.setComposition(copperPowder, copperComp);
        AlloyComposition.setContamination(copperPowder, 0.0);
        output.accept(copperPowder);

        // Example 5: Metal Fragment - 25% of an ingot
        ItemStack fragment = new ItemStack(MetallurgyItems.METAL_FRAGMENT.get());
        Map<String, Double> fragmentComp = new HashMap<>();
        fragmentComp.put("minecraft:gold_ingot", 1.0);
        AlloyComposition.setComposition(fragment, fragmentComp);
        AlloyComposition.setFragmentPercentage(fragment, 0.25); // 25% of an ingot
        AlloyComposition.setContamination(fragment, 0.0);
        output.accept(fragment);
    }

    private static void addToExistingTabs(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();

        if (tabKey == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            // Add chisels to vanilla tools tab
            CreativeTabDeferredRegister.addToDisplay(event, MetallurgyItems.IRON_CHISEL);
            CreativeTabDeferredRegister.addToDisplay(event, MetallurgyItems.DIAMOND_CHISEL);
            CreativeTabDeferredRegister.addToDisplay(event, MetallurgyItems.NETHERITE_CHISEL);
        } else if (tabKey == CreativeModeTabs.INGREDIENTS) {
            // Add metal processing items to vanilla ingredients tab
            CreativeTabDeferredRegister.addToDisplay(event, MetallurgyItems.ALLOY_POWDER);
            CreativeTabDeferredRegister.addToDisplay(event, MetallurgyItems.METAL_FRAGMENT);
            CreativeTabDeferredRegister.addToDisplay(event, MetallurgyItems.ALLOY_INGOT);
        }
    }
}
