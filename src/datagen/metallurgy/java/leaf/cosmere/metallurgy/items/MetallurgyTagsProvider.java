package leaf.cosmere.metallurgy.items;

import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.registries.MetallurgyItems;
import leaf.cosmere.tag.BaseTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class MetallurgyTagsProvider extends BaseTagProvider {
    public MetallurgyTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, Metallurgy.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags(HolderLookup.Provider registries) {
        getItemBuilder(Tags.Items.TOOLS).add(
                MetallurgyItems.IRON_CHISEL.asItem(),
                MetallurgyItems.DIAMOND_CHISEL.asItem(),
                MetallurgyItems.NETHERITE_CHISEL.asItem());

        // Future: Add custom tags for metallurgy-specific items
        // Example: forge:dusts for metal powder
        // Example: forge:fragments for metal fragments

        getItemBuilder(Tags.Items.DUSTS).add(
                MetallurgyItems.ALLOY_POWDER.asItem());
        getItemBuilder(Tags.Items.INGOTS).add(
                MetallurgyItems.ALLOY_INGOT.asItem(),
                MetallurgyItems.ALLOY_FRAGMENT.asItem());
    }
}
