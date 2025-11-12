package leaf.cosmere.metallurgy;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.metallurgy.common.Metallurgy;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class MetallurgyEngLangGen extends LanguageProvider {
    public MetallurgyEngLangGen(PackOutput output) {
        super(output, Metallurgy.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItemsAndBlocks();
        addItemGroups();
        addTooltips();
    }

    private void addItemsAndBlocks() {
        // Items and Blocks
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            final ResourceLocation registryName = RegistryHelper.get(item);
            if (registryName.getNamespace().contentEquals(Metallurgy.MODID)) {
                String localisedString = StringHelper.fixCapitalisation(registryName.getPath());
                add(item.getDescriptionId(), localisedString);
            }
        }
    }

    private void addItemGroups() {
        add("tabs.metallurgy.items", "Metallurgy");
    }

    private void addTooltips() {
        // Future: Add any custom tooltip translations here
    }
}
