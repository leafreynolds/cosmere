/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.items;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class HemalurgyItemTagsGen extends ItemTagsProvider
{
	public HemalurgyItemTagsGen(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, blockTagsProvider, Hemalurgy.MODID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasHemalurgicEffect())
			{
				HemalurgicSpikeItem spikeItem = HemalurgyItems.METAL_SPIKE.get(metalType).asItem();
				add(CosmereTags.Items.METAL_SPIKE, spikeItem);

				add(CosmereTags.Items.CURIO_HEAD, spikeItem);
				add(CosmereTags.Items.CURIO_LINCHPIN, spikeItem);
				add(CosmereTags.Items.CURIO_BACK, spikeItem);
				add(CosmereTags.Items.CURIO_BODY, spikeItem);
				add(CosmereTags.Items.CURIO_BRACELET, spikeItem);
				add(CosmereTags.Items.CURIO_HANDS, spikeItem);
				add(CosmereTags.Items.CURIO_LEGS, spikeItem);
				add(CosmereTags.Items.CURIO_FEET, spikeItem);
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