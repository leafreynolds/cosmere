/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.allomancy;

import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.tag.BaseTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class AllomancyTagProvider extends BaseTagProvider
{
	public AllomancyTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, Allomancy.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags(HolderLookup.Provider registries)
	{
		getItemBuilder(CosmereTags.Items.CURIO_HEAD).add(AllomancyItems.MISTCLOAK.asItem());
	}
}