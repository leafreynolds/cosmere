/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.items.HemalurgicSpikeItem;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import leaf.cosmere.tag.BaseTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class HemalurgyItemTagsProvider extends BaseTagProvider
{
	public HemalurgyItemTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, Hemalurgy.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags()
	{
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasHemalurgicEffect())
			{
				HemalurgicSpikeItem spikeItem = HemalurgyItems.METAL_SPIKE.get(metalType).asItem();
				getItemBuilder(CosmereTags.Items.METAL_SPIKE).add(spikeItem);


				if (metalType.isPhysicalSpike())
				{
					getItemBuilder(CosmereTags.Items.CURIO_EYES).add(spikeItem);
					getItemBuilder(CosmereTags.Items.CURIO_PHYSICAL).add(spikeItem);
					//any spike can be a linchpin?
					getItemBuilder(CosmereTags.Items.CURIO_LINCHPIN).add(spikeItem);
				}
				if (metalType.isMentalSpike())
				{
					getItemBuilder(CosmereTags.Items.CURIO_MENTAL).add(spikeItem);
				}
				if (metalType.isSpiritualSpike())
				{
					getItemBuilder(CosmereTags.Items.CURIO_SPIRITUAL).add(spikeItem);
				}
				if (metalType.isTemporalSpike())
				{
					getItemBuilder(CosmereTags.Items.CURIO_TEMPORAL).add(spikeItem);
				}
			}
		}

		//we do know gold spikes can be used as a linchpin
		getItemBuilder(CosmereTags.Items.CURIO_LINCHPIN).add(HemalurgyItems.METAL_SPIKE.get(Metals.MetalType.GOLD).get());
	}


}