/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.items;

import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.aviar.common.Aviar;
import leaf.cosmere.aviar.common.registries.AviarItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class AviarItemModelsGen extends ItemModelProvider
{

	public AviarItemModelsGen(DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, Aviar.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		for (IItemProvider itemRegistryObject : AviarItems.ITEMS.getAllItems())
		{
			String path = itemRegistryObject.getRegistryName().getPath();
			Item item = itemRegistryObject.asItem();

			if (item instanceof ForgeSpawnEggItem)
			{
				getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));

				continue;
			}

			//else normal item texture rules apply
			simpleItem(path, path);
		}

	}

	public String getPath(Supplier<? extends Item> itemSupplier)
	{
		ResourceLocation location = ResourceLocationHelper.get(itemSupplier.get());
		return location.getPath();
	}

	public ItemModelBuilder simpleItem(String path, String texturePath)
	{
		return this.getBuilder(path)
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", modLoc("item/" + texturePath));
	}
}