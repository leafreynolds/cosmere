/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.items.MetalIngotItem;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
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
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{

			if (metalType.hasMaterialItem())
			{
				MetalIngotItem ingotItem = ItemsRegistry.METAL_INGOTS.get(metalType).asItem();

				//don't need to tell the tag what each individual item is if they're tagged correctly
				//add(Tags.Items.INGOTS, ingotItem);

				// tell our ingots what their tags are.
				final TagKey<Item> metalIngotTag = metalType.getMetalIngotTag();
				add(metalIngotTag, ingotItem);

				//tell the ingots that our ingot tags are part of them
				this.tag(Tags.Items.INGOTS).addTag(metalIngotTag);

				//tell the nuggets that our nugget tags are part of them
				final TagKey<Item> metalNuggetTag = metalType.getMetalNuggetTag();
				this.tag(Tags.Items.NUGGETS).addTag(metalNuggetTag);

				// tell the Nugget that our Nuggets are related
				Item nuggetItem = ItemsRegistry.METAL_NUGGETS.get(metalType).asItem();

				//don't need to tell the tag what each individual item is if they're tagged correctly
				//add(Tags.Items.NUGGETS, nuggetItem);

				// tell our nuggets what their tags are.
				add(metalNuggetTag, nuggetItem);

				//associate block tags with their item counterparts
				final TagKey<Item> storageBlockTag = CosmereTags.Items.METAL_BLOCK_ITEM_TAGS.get(metalType);
				this.copy(CosmereTags.Blocks.METAL_BLOCK_TAGS.get(metalType), storageBlockTag);
				this.tag(Tags.Items.STORAGE_BLOCKS).addTag(storageBlockTag);
			}

			if (metalType.hasOre())
			{
				Item item = ItemsRegistry.METAL_RAW_ORE.get(metalType).asItem();
				add(metalType.getMetalRawTag(), item);
			}

			if (metalType.isAlloy())
			{
				Item item = ItemsRegistry.METAL_RAW_BLEND.get(metalType).asItem();
				add(metalType.getMetalBlendTag(), item);
			}
		}

	}

	public void add(TagKey<Item> branch, Item item)
	{
		this.tag(branch).add(item);
	}

	public void add(TagKey<Item> branch, Item... item)
	{
		this.tag(branch).add(item);
	}


}