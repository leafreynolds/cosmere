/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.example;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.example.common.Example;
import leaf.cosmere.example.common.registries.ExampleItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ExampleItemModelsGen extends ItemModelProvider
{

	public ExampleItemModelsGen(PackOutput generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, Example.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		for (IItemProvider itemRegistryObject : ExampleItems.ITEMS.getAllItems())
		{
			String path = itemRegistryObject.getRegistryName().getPath();
			Item item = itemRegistryObject.asItem();

			if (item instanceof ForgeSpawnEggItem)
			{
				getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
				continue;
			}
			//if (item instanceof modelOverrideType)
			//{
			//	this.getBuilder(path)
			//			.parent(new ModelFile.UncheckedModelFile("example:item/specificModelParent"))
			//			.texture("layer0", modLoc("item/" + "texture_name"));
			//	continue;
			//}
			//else if (item instanceof hardcodedModelType)
			//{
			//	//skip
			//	continue;
			//}

			//else normal item texture rules apply
			simpleItem(path, path);
		}

	}

	public String getPath(Supplier<? extends Item> itemSupplier)
	{
		ResourceLocation location = RegistryHelper.get(itemSupplier.get());
		return location.getPath();
	}

	public ItemModelBuilder simpleItem(String path, String texturePath)
	{
		return this.getBuilder(path)
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", modLoc("item/" + texturePath));
	}
}