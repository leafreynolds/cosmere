/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.items;

import leaf.cosmere.Cosmere;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.items.MetalIngotItem;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.items.gems.PolestoneItem;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTagsGen extends ItemTagsProvider
{

    public ItemTagsGen(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, blockTagsProvider, Cosmere.MODID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {

        add(TagsRegistry.Items.CURIO_BRACELET, ItemsRegistry.BANDS_OF_MOURNING.get());

        for (Metals.MetalType metalType : Metals.MetalType.values())
        {
            if (metalType.hasFeruchemicalEffect())
            {
                //curio stuff
                add(TagsRegistry.Items.CURIO_NECKLACE, metalType.getNecklaceItem());
                add(TagsRegistry.Items.CURIO_RING, metalType.getRingItem());
                add(TagsRegistry.Items.CURIO_BRACELET, metalType.getBraceletItem());
            }

            if (metalType.hasHemalurgicEffect())
            {
                HemalurgicSpikeItem spikeItem = metalType.getSpikeItem();

                add(TagsRegistry.Items.CURIO_ANY, spikeItem);
                add(TagsRegistry.Items.METAL_SPIKE, spikeItem);
            }
            // tell the ingots that our ingots are related

            if (metalType.hasMaterialItem())
            {
                MetalIngotItem ingotItem = metalType.getIngotItem();
                add(Tags.Items.INGOTS, ingotItem);
                // tell our ingots what their tags are.
                add(metalType.getMetalIngotTag(), ingotItem);

                //tell the ingots that our ingot tags are part of them
                this.tag(Tags.Items.INGOTS).addTag(metalType.getMetalIngotTag());

                //tell the nuggets that our nugget tags are part of them
                this.tag(Tags.Items.NUGGETS).addTag(metalType.getMetalNuggetTag());

                // tell the Nugget that our Nuggets are related
                Item item = metalType.getNuggetItem();
                add(Tags.Items.NUGGETS, item);
                // tell our ingots what their tags are.
                add(metalType.getMetalNuggetTag(), item);

                //associate block tags with their item counterparts
                this.copy(TagsRegistry.Blocks.METAL_BLOCK_TAGS.get(metalType), TagsRegistry.Items.METAL_BLOCK_ITEM_TAGS.get(metalType));


                //not sure why this is needed, but botania had it ^_^;;
                this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
            }

            if (metalType.isAlloy() || metalType.hasOre())
            {
                add(metalType.getMetalRawTag(), metalType.getRawMetalItem());
            }
        }

        for (Roshar.Polestone polestone : Roshar.Polestone.values())
        {
            for (Roshar.GemSize size : Roshar.GemSize.values())
            {
                final Item polestoneItem = polestone.getPolestoneItem(size);
                add(Tags.Items.GEMS, polestoneItem);
                add(TagsRegistry.Items.GEM_TAGS.get(polestone), polestoneItem);
            }
        }
    }

    public void add(ITag.INamedTag<Item> branch, Item item)
    {
        this.tag(branch).add(item);
    }

    public void add(ITag.INamedTag<Item> branch, Item... item)
    {
        this.tag(branch).add(item);
    }


}