/*
 * File updated ~ 11 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.items;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class FeruchemyItemTagsGen extends ItemTagsProvider
{

	public FeruchemyItemTagsGen(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, blockTagsProvider, Feruchemy.MODID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		add(CosmereTags.Items.CURIO_BRACELET, FeruchemyItems.BANDS_OF_MOURNING.get());

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasFeruchemicalEffect())
			{
				//curio stuff
				add(CosmereTags.Items.CURIO_NECKLACE, FeruchemyItems.METAL_NECKLACES.get(metalType).asItem());
				add(CosmereTags.Items.CURIO_RING, FeruchemyItems.METAL_RINGS.get(metalType).asItem());
				add(CosmereTags.Items.CURIO_BRACELET, FeruchemyItems.METAL_BRACELETS.get(metalType).asItem());
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