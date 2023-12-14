/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.items;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class SandmasteryItemTagsGen extends ItemTagsProvider
{
	public SandmasteryItemTagsGen(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, blockTagsProvider, Sandmastery.MODID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		add(CosmereTags.Items.CURIO_BELT, SandmasteryItems.SAND_POUCH_ITEM.asItem());
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