/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.items;

import leaf.cosmere.Cosmere;
import leaf.cosmere.items.MetalIngotItem;
import leaf.cosmere.items.MetalNuggetItem;
import leaf.cosmere.items.MetalRawOreItem;
import leaf.cosmere.items.ShardbladeItem;
import leaf.cosmere.items.curio.BraceletMetalmindItem;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.items.curio.NecklaceMetalmindItem;
import leaf.cosmere.items.curio.RingMetalmindItem;
import leaf.cosmere.items.gems.PolestoneItem;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ItemModelsGen extends ItemModelProvider
{

    public ItemModelsGen(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, Cosmere.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        for (RegistryObject<Item> itemRegistryObject : ItemsRegistry.ITEMS.getEntries())
        {
            String path = getPath(itemRegistryObject);
            Item item = itemRegistryObject.get();

            //blocks have their own model rules
            if (item instanceof BlockItem)
            {
                continue;
            }
            //otherwise set specific textures based on these item types
            else if (item instanceof MetalIngotItem)
            {
                simpleItem(path, "metal_ingot");
                continue;
            }
            else if (item instanceof MetalNuggetItem)
            {
                simpleItem(path, "metal_nugget");
                continue;
            }
            else if (item instanceof BraceletMetalmindItem)
            {
                simpleItem(path, "metal_bracelet");
                continue;
            }
            else if (item instanceof RingMetalmindItem)
            {
                simpleItem(path, "metal_ring");
                continue;
            }
            else if (item instanceof NecklaceMetalmindItem)
            {
                simpleItem(path, "metal_necklace");
                continue;
            }
            else if (item instanceof HemalurgicSpikeItem)
            {
                simpleItem(path, "metal_spike");
                continue;
            }
            else if (item instanceof ShardbladeItem)
            {
                continue;
            }
            else if (item instanceof MetalRawOreItem)
            {
                MetalRawOreItem rawItem = (MetalRawOreItem)item;
                simpleItem(path, rawItem.getMetalType().isAlloy() ? "metal_blend" : "metal_raw");
                continue;
            }
            else if (item instanceof PolestoneItem)
            {
                switch (((PolestoneItem)item).getSize())
                {
                    case BROAM:
                        simpleItem(path, "polestone_broam");
                        break;
                    case MARK:
                        simpleItem(path, "polestone_mark");
                        break;
                    case CHIP:
                        simpleItem(path, "polestone_chip");
                        break;
                }
                continue;
            }

            //else normal item texture rules apply
            simpleItem(path, path);
        }

    }

    public String getPath(Supplier<? extends Item> itemSupplier)
    {
        ResourceLocation location = itemSupplier.get().getRegistryName();
        return location.getPath();
    }

    public ItemModelBuilder simpleItem(String path, String texturePath)
    {
        return this.getBuilder(path)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + texturePath));
    }
}