/*
 * File updated ~ 7 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.tag.BaseTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class AllomancyTagProvider extends BaseTagProvider
{
	public AllomancyTagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(dataGenerator, Allomancy.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags()
	{
		getItemBuilder(CosmereTags.Items.CURIO_HEAD).add(AllomancyItems.MISTCLOAK.asItem());
	}
}