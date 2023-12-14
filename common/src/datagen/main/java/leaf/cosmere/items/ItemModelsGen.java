/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.api.helpers.ResourceLocationHelper;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.items.MetalIngotItem;
import leaf.cosmere.common.items.MetalNuggetItem;
import leaf.cosmere.common.items.MetalRawOreItem;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ItemModelsGen extends ItemModelProvider
{

	public ItemModelsGen(DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, Cosmere.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		for (IItemProvider itemRegistryObject : ItemsRegistry.ITEMS.getAllItems())
		{
			String path = itemRegistryObject.getRegistryName().getPath();
			Item item = itemRegistryObject.asItem();

			//blocks have their own model rules
			if (item instanceof BlockItem)
			{
				continue;
			}
			//otherwise set specific textures based on these item types
			else if (item instanceof MetalIngotItem)
			{
				simpleItem(path, "metal_ingot");
				continue;
			}
			else if (item instanceof MetalNuggetItem)
			{
				simpleItem(path, "metal_nugget");
				continue;
			}
			else if (item instanceof MetalRawOreItem rawItem)
			{
				simpleItem(path, rawItem.getMetalType().isAlloy() ? "metal_blend" : "metal_raw");
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