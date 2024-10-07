/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.items.BraceletMetalmindItem;
import leaf.cosmere.feruchemy.common.items.NecklaceMetalmindItem;
import leaf.cosmere.feruchemy.common.items.RingMetalmindItem;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FeruchemyItemModelsGen extends ItemModelProvider
{

	public FeruchemyItemModelsGen(PackOutput generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, Feruchemy.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		for (IItemProvider itemRegistryObject : FeruchemyItems.ITEMS.getAllItems())
		{
			String path = itemRegistryObject.getRegistryName().getPath();
			Item item = itemRegistryObject.asItem();

			//blocks have their own model rules
			if (item instanceof BlockItem)
			{
				continue;
			}
			else if (item instanceof BraceletMetalmindItem)
			{
				simpleItem(path, "metal_bracelet");
				continue;
			}
			else if (item instanceof RingMetalmindItem)
			{
				simpleItem(path, "metal_ring");
				continue;
			}
			else if (item instanceof NecklaceMetalmindItem)
			{
				simpleItem(path, "metal_necklace");
				continue;
			}

			//else normal item texture rules apply
			simpleItem(path, path);
		}

	}

	public ItemModelBuilder simpleItem(String path, String texturePath)
	{
		return this.getBuilder(path)
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", modLoc("item/" + texturePath));
	}
}