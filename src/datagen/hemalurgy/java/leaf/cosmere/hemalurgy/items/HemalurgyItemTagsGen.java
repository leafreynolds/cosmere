/*
 * File updated ~ 11 - 11 - 2023 ~ Leaf
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


				if (metalType.isPhysicalSpike())
				{
					add(CosmereTags.Items.CURIO_EYES, spikeItem);
					add(CosmereTags.Items.CURIO_PHYSICAL, spikeItem);
					//any spike can be a linchpin?
					add(CosmereTags.Items.CURIO_LINCHPIN, spikeItem);
				}
				if (metalType.isMentalSpike())
				{
					add(CosmereTags.Items.CURIO_MENTAL, spikeItem);
				}
				if (metalType.isSpiritualSpike())
				{
					add(CosmereTags.Items.CURIO_SPIRITUAL, spikeItem);
				}
				if (metalType.isTemporalSpike())
				{
					add(CosmereTags.Items.CURIO_TEMPORAL, spikeItem);
				}
			}
		}

		//we do know gold spikes can be used as a linchpin
		add(CosmereTags.Items.CURIO_LINCHPIN, HemalurgyItems.METAL_SPIKE.get(Metals.MetalType.GOLD).get());
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