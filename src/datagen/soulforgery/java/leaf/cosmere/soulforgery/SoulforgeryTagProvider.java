/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.soulforgery;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.soulforgery.common.Soulforgery;
import leaf.cosmere.soulforgery.common.registries.SoulforgeryBlocks;
import leaf.cosmere.tag.BaseTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SoulforgeryTagProvider extends BaseTagProvider
{
	public SoulforgeryTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, Soulforgery.MODID, existingFileHelper);
	}


	@Override
	protected List<IBlockProvider> getAllBlocks()
	{
		return SoulforgeryBlocks.BLOCKS.getAllBlocks();
	}


	@Override
	protected void registerTags(HolderLookup.Provider registries)
	{
		//getItemBuilder(CosmereTags.Items.CURIO_HEAD).add(Soulforgery.Item.asItem());

		addItems();
		addBlocks();
		addStorageBlocks();
		addEntityTypes();
		addGameEvents();

		addContainsMetal();
	}


	private void addItems()
	{

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