/*
 * File updated ~ 6 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.GodMetalNuggetItem;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.data.PackOutput;
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

	public ItemModelsGen(PackOutput packOutput, ExistingFileHelper existingFileHelper)
	{
		super(packOutput, Cosmere.MODID, existingFileHelper);
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
			else if (item instanceof GodMetalAlloyNuggetItem godMetalAlloyNuggetItem)
			{
				simpleItem(path, godMetalAlloyNuggetItem.getMetalType().getName() + "_" + godMetalAlloyNuggetItem.getAlloyedMetalType().getName() + "_nugget");
				continue;
			}
			else if (item instanceof GodMetalNuggetItem godMetalNuggetItem)
			{
				simpleItem(path, godMetalNuggetItem.getMetalType().getName() + "_nugget");
				continue;
			}
			////otherwise set specific textures based on these item types
			//else if (item instanceof MetalIngotItem)
			//{
			//	simpleItem(path, "metal_ingot");
			//	continue;
			//}
			//else if (item instanceof MetalNuggetItem)
			//{
			//	simpleItem(path, "metal_nugget");
			//	continue;
			//}
			//else if (item instanceof MetalRawOreItem rawItem)
			//{
			//	simpleItem(path, rawItem.getMetalType().isAlloy() ? "metal_blend" : "metal_raw");
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