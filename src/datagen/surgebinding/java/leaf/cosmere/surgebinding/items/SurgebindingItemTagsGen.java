/*
 * File updated ~ 19 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.items;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.items.GemstoneItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class SurgebindingItemTagsGen extends ItemTagsProvider
{

	public SurgebindingItemTagsGen(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, blockTagsProvider, Surgebinding.MODID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		for (Roshar.Gemstone gemstone : Roshar.Gemstone.values())
		{
			final GemstoneItem broamItem = SurgebindingItems.GEMSTONE_BROAMS.get(gemstone).asItem();

			add(Tags.Items.GEMS, broamItem);
			add(Tags.Items.GEMS, SurgebindingItems.GEMSTONE_MARKS.get(gemstone).asItem());
			add(Tags.Items.GEMS, SurgebindingItems.GEMSTONE_CHIPS.get(gemstone).asItem());

			//and let our full sized gems be usable for other recipes
			add(CosmereTags.Items.GEM_TAGS.get(gemstone), broamItem);


			//associate block tags with their item counterparts
			final TagKey<Item> storageBlockTag = CosmereTags.Items.GEM_BLOCK_ITEM_TAGS.get(gemstone);
			this.copy(CosmereTags.Blocks.GEM_BLOCK_TAGS.get(gemstone), storageBlockTag);
			this.tag(Tags.Items.STORAGE_BLOCKS).addTag(storageBlockTag);
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