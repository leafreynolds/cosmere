/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import leaf.cosmere.tag.BaseTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class FeruchemyTagProvider extends BaseTagProvider
{
	public FeruchemyTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, Feruchemy.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags(HolderLookup.Provider registries)
	{
		//getItemBuilder(CosmereTags.Items.CURIO_HEAD).add(Example.Item.asItem());

		getItemBuilder(CosmereTags.Items.CURIO_BRACELET).add(FeruchemyItems.BANDS_OF_MOURNING.get());

		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
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