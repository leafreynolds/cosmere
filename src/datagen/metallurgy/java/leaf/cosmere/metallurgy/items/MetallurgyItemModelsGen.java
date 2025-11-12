package leaf.cosmere.metallurgy.items;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.registries.MetallurgyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class MetallurgyItemModelsGen extends ItemModelProvider {
    public MetallurgyItemModelsGen(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, Metallurgy.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (IItemProvider itemRegistryObject : MetallurgyItems.ITEMS.getAllItems()) {
            String path = itemRegistryObject.getRegistryName().getPath();
            Item item = itemRegistryObject.asItem();

            // Blocks have their own model rules
            if (item instanceof BlockItem) {
                continue;
            }

            // All metallurgy items use simple generated item models
            simpleItem(path, path);
        }
    }

    public String getPath(Supplier<? extends Item> itemSupplier) {
        ResourceLocation location = RegistryHelper.get(itemSupplier.get());
        return location.getPath();
    }

    public ItemModelBuilder simpleItem(String path, String texturePath) {
        return this.getBuilder(path)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + texturePath));
    }

    public ItemModelBuilder handheldItem(String path, String texturePath) {
        return this.getBuilder(path)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", modLoc("item/" + texturePath));
    }
}
