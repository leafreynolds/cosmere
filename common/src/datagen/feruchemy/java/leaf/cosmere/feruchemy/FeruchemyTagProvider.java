/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import leaf.cosmere.tag.BaseTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class FeruchemyTagProvider extends BaseTagProvider
{
	public FeruchemyTagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, Feruchemy.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags()
	{
		//getItemBuilder(CosmereTags.Items.CURIO_HEAD).add(Example.Item.asItem());

		getItemBuilder(CosmereTags.Items.CURIO_BRACELET).add(FeruchemyItems.BANDS_OF_MOURNING.get());

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (metalType.hasFeruchemicalEffect())
			{
				//curio stuff
				getItemBuilder(CosmereTags.Items.CURIO_NECKLACE).add(FeruchemyItems.METAL_NECKLACES.get(metalType).asItem());
				getItemBuilder(CosmereTags.Items.CURIO_RING).add(FeruchemyItems.METAL_RINGS.get(metalType).asItem());
				getItemBuilder(CosmereTags.Items.CURIO_BRACELET).add(FeruchemyItems.METAL_BRACELETS.get(metalType).asItem());
			}
		}
	}
}