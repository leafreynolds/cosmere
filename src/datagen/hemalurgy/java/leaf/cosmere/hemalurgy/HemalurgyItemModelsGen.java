/*
 * File updated ~ 9 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.hemalurgy;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.hemalurgy.common.Hemalurgy;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class HemalurgyItemModelsGen extends ItemModelProvider
{

	public HemalurgyItemModelsGen(PackOutput packOutput, ExistingFileHelper existingFileHelper)
	{
		super(packOutput, Hemalurgy.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels()
	{
		for (IItemProvider itemRegistryObject : HemalurgyItems.ITEMS.getAllItems())
		{
			String path = itemRegistryObject.getRegistryName().getPath();
			Item item = itemRegistryObject.asItem();

			if (item instanceof DeferredSpawnEggItem)
			{
				getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
				continue;
			}
			//if (item instanceof HemalurgicSpikeItem)
			//{
			//	this.getBuilder(path)
			//			.parent(new ModelFile.UncheckedModelFile("hemalurgy:item/spike"))
			//			.texture("layer0", modLoc("item/" + "metal_spike"));
			//	continue;
			//}
			else if (item instanceof SwordItem)
			{
				//koloss sword
				continue;
			}

			//else normal item texture rules apply
			spikeItem(path, path);
		}

	}

	public String getPath(Supplier<? extends Item> itemSupplier)
	{
		ResourceLocation location = RegistryHelper.get(itemSupplier.get());
		return location.getPath();
	}

	private ItemModelBuilder spikeItem(String path, String texturePath)
	{
		return this.getBuilder(path)
				.parent(new ModelFile.UncheckedModelFile("minecraft:item/handheld"))
				.texture("layer0", modLoc("item/" + texturePath))
				.transforms()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
				.rotation(33.75f, -90f, 0f)
				.scale(0.5f, 0.5f, 0.5f)
				.translation(0f, 2f, 1f)
				.end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
				.rotation(0f, -90f, 0f)
				.scale(0.5f, 0.5f, 0.5f)
				.translation(0f, 2f, 1f)
				.end()
				.end();
	}
}
