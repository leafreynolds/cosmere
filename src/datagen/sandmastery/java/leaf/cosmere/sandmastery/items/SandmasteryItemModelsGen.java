/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.sandmastery.items;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.items.SandJarItem;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class SandmasteryItemModelsGen extends ItemModelProvider
{

	public SandmasteryItemModelsGen(PackOutput generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, Sandmastery.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		for (IItemProvider itemRegistryObject : SandmasteryItems.ITEMS.getAllItems())
		{
			String path = itemRegistryObject.getRegistryName().getPath();
			Item item = itemRegistryObject.asItem();

			if (item instanceof BlockItem)
			{
				continue;
			}
			if (item instanceof SandJarItem)
			{
				continue;
			}
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
