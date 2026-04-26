/*
 * File updated ~ 9 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.feruchemy;

import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import leaf.cosmere.feruchemy.common.Feruchemy;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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
			else if (item instanceof ChargeableMetalCurioItem)
			{
				//because I'm lazy and don't wanna rename all the textures
				//doesn't really need to be that explicit in naming anyway
				//basically just remove _metalmind from the end if it exists
				simpleItem(path, path.replace("_metalmind", ""));
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
