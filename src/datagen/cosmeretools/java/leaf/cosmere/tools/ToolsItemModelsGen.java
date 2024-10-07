/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.registries.ToolsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ToolsItemModelsGen extends ItemModelProvider
{

	public ToolsItemModelsGen(PackOutput generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, CosmereTools.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		for (IItemProvider itemRegistryObject : ToolsItems.ITEMS.getAllItems())
		{
			String path = itemRegistryObject.getRegistryName().getPath();
			Item item = itemRegistryObject.asItem();

			if (item instanceof ForgeSpawnEggItem)
			{
				getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
				continue;
			}
			else if (item instanceof PickaxeItem)
			{
				toolItem(path, "metal_pickaxe");
				continue;
			}
			else if (item instanceof ShovelItem)
			{
				toolItem(path, "metal_shovel");
				continue;
			}
			else if (item instanceof AxeItem)
			{
				toolItem(path, "metal_axe");
				continue;
			}
			else if (item instanceof HoeItem)
			{
				toolItem(path, "metal_hoe");
				continue;
			}
			else if (item instanceof SwordItem)
			{
				toolItem(path, "metal_sword");
				continue;
			}
			else if (item instanceof ArmorItem)
			{
				if (path.contains("helmet"))
				{
					simpleItemMC(path, "iron_helmet");
				}
				else if (path.contains("chestplate"))
				{
					simpleItemMC(path, "iron_chestplate");
				}
				else if (path.contains("leggings"))
				{
					simpleItemMC(path, "iron_leggings");
				}
				else if (path.contains("boots"))
				{
					simpleItemMC(path, "iron_boots");
				}

				continue;
			}

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

	public ItemModelBuilder simpleItemMC(String path, String texturePath)
	{
		return this.getBuilder(path)
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", new ResourceLocation("item/" + texturePath));
	}

	public ItemModelBuilder toolItem(String path, String toolTypePath)
	{
		return this.getBuilder(path)
				.parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", modLoc("item/" + toolTypePath))
				.texture("layer1", modLoc("item/" + toolTypePath + "_0"));
	}
}