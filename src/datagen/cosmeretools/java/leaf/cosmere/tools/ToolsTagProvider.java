/*
 * File updated ~ 28 - 3 - 2026 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.tag.BaseTagProvider;
import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.registries.ToolsBlocks;
import leaf.cosmere.tools.common.registries.ToolsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ToolsTagProvider extends BaseTagProvider
{
	public ToolsTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, CosmereTools.MODID, existingFileHelper);
	}


	@Override
	protected List<IBlockProvider> getAllBlocks()
	{
		return ToolsBlocks.BLOCKS.getAllBlocks();
	}


	@Override
	protected void registerTags(HolderLookup.Provider registries)
	{
		//getItemBuilder(CosmereTags.Items.CURIO_HEAD).add(Tools.Item.asItem());

		addItems();
		addBlocks();
		addStorageBlocks();
		addEntityTypes();
		addGameEvents();

		addContainsMetal();
	}


	private void addItems()
	{
		for (var item : ToolsItems.METAL_SWORDS.values())
			getItemBuilder(ItemTags.SWORDS).add(item);

		for (var item : ToolsItems.METAL_AXES.values())
			getItemBuilder(ItemTags.AXES).add(item);

		for (var item : ToolsItems.METAL_SHOVEL.values())
			getItemBuilder(ItemTags.SHOVELS).add(item);

		for (var item : ToolsItems.METAL_PICKAXES.values())
			getItemBuilder(ItemTags.PICKAXES).add(item);

		for (var item : ToolsItems.METAL_HOE.values())
			getItemBuilder(ItemTags.HOES).add(item);

	}

	private void addBlocks()
	{
		//addToTag(BlockTags.NEEDS_STONE_TOOL, BlocksRegistry.METALWORKING_TABLE);
		//addToHarvestTag(BlockTags.MINEABLE_WITH_AXE, BlocksRegistry.METALWORKING_TABLE);

	}

	private void addEntityTypes()
	{
		//getEntityTypeBuilder(CosmereTags.EntityTypes.CONTAINS_METAL).add(EntityType.IRON_GOLEM);
	}

	private void addGameEvents()
	{

	}

	private void addStorageBlocks()
	{

	}

	private void addContainsMetal()
	{

	}
}