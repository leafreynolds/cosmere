
/*
 * File updated ~ 20 - 11 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding;

import com.google.common.collect.ImmutableList;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.registration.impl.BlockRegistryObject;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.blocks.GemBlock;
import leaf.cosmere.surgebinding.common.items.GemstoneItem;
import leaf.cosmere.surgebinding.common.items.ShardplateCurioItem;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBiomes;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import leaf.cosmere.tag.BaseTagProvider;
import leaf.cosmere.tag.IntrinsicCosmereTagBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SurgebindingTagsProvider extends BaseTagProvider
{
	public SurgebindingTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, Surgebinding.MODID, existingFileHelper);
	}

	@Override
	protected List<IBlockProvider> getAllBlocks()
	{
		return SurgebindingBlocks.BLOCKS.getAllBlocks();
	}

	@Override
	protected void registerTags(HolderLookup.Provider registries)
	{
		addItems();
		addBlocks();
		addStorageBlocks();
		addEntityTypes();
		addGameEvents();
		addBiomes();

		addContainsMetal();

		SurgebindingItems.SHARDPLATE_SUITS.values().forEach(
				item -> getItemBuilder(CosmereTags.Items.CURIO_SHARDPLATE).add(item.asItem())
		);
	}

	private void addItems()
	{
		for (Roshar.Gemstone gemstone : EnumUtils.GEMSTONE_TYPES)
		{
			final ItemRegistryObject<GemstoneItem> broamItem = SurgebindingItems.GEMSTONE_BROAMS.get(gemstone);

			addToTag(Tags.Items.GEMS, broamItem);
			addToTag(Tags.Items.GEMS, SurgebindingItems.GEMSTONE_MARKS.get(gemstone));
			addToTag(Tags.Items.GEMS, SurgebindingItems.GEMSTONE_CHIPS.get(gemstone));

			//and let our full sized gems be usable for other recipes
			addToTag(CosmereTags.Items.GEM_TAGS.get(gemstone), broamItem);
		}

		for (Roshar.RadiantOrder order: EnumUtils.RADIANT_ORDERS)
		{
			final ItemRegistryObject<ShardplateCurioItem> shardplateItem = SurgebindingItems.SHARDPLATE_SUITS.get(order);

			addToTag(Tags.Items.ARMORS, shardplateItem);
			addToTag(CosmereTags.Items.CURIO_SHARDPLATE, shardplateItem);
		}
	}

	private void addBlocks()
	{
		for (Roshar.Gemstone gemstone : EnumUtils.GEMSTONE_TYPES)
		{
			final BlockRegistryObject<GemBlock, BlockItem> gemBlock = SurgebindingBlocks.GEM_BLOCKS.get(gemstone);
			//final BlockRegistryObject<GemOreBlock, BlockItem> gemOre = SurgebindingBlocks.GEM_ORE.get(gemstone);
			//final BlockRegistryObject<GemOreBlock, BlockItem> gemOreDeepslate = SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(gemstone);

			var list = ImmutableList.of(gemBlock);//, gemOre, gemOreDeepslate);

			for (var block : list)
			{
				addToTag(CosmereTags.Blocks.DRAGON_PROOF, block);
				addToHarvestTag(BlockTags.MINEABLE_WITH_PICKAXE, block);
				addToTag(BlockTags.NEEDS_IRON_TOOL, block);
			}

			addToTag(BlockTags.BEACON_BASE_BLOCKS, gemBlock);
			//addToTag(CosmereTags.Blocks.GEM_ORE_BLOCK_TAGS.get(gemstone), gemOre, gemOreDeepslate);
		}

		for (BlockRegistryObject<?, BlockItem> plantBlock : SurgebindingBlocks.PLANT_BLOCKS)
		{
			addToHarvestTag(BlockTags.MINEABLE_WITH_AXE, plantBlock);
			addToHarvestTag(BlockTags.MINEABLE_WITH_PICKAXE, plantBlock);
			addToHarvestTag(BlockTags.MINEABLE_WITH_SHOVEL, plantBlock);
		}
	}

	private void addStorageBlocks()
	{
		final IntrinsicCosmereTagBuilder<Item> itemBuilder = getItemBuilder(Tags.Items.STORAGE_BLOCKS);
		final IntrinsicCosmereTagBuilder<Block> blockBuilder = getBlockBuilder(Tags.Blocks.STORAGE_BLOCKS);

		for (Roshar.Gemstone gemstone : EnumUtils.GEMSTONE_TYPES)
		{
			final TagKey<Item> storageBlockItemTag = CosmereTags.Items.GEM_BLOCK_ITEM_TAGS.get(gemstone);
			final TagKey<Block> storageBlockTag = CosmereTags.Blocks.GEM_BLOCK_TAGS.get(gemstone);
			final BlockRegistryObject<GemBlock, BlockItem> blockRegistryObject = SurgebindingBlocks.GEM_BLOCKS.get(gemstone);

			addToTags(storageBlockItemTag, storageBlockTag, blockRegistryObject);

			itemBuilder.add(storageBlockItemTag);
			blockBuilder.add(storageBlockTag);
		}
	}

	private void addEntityTypes()
	{
		//getEntityTypeBuilder(CosmereTags.EntityTypes.CONTAINS_METAL).add(EntityType.IRON_GOLEM);
	}

	private void addGameEvents()
	{

	}

	private void addBiomes()
	{
		getBiomeBuilder(CosmereTags.Biomes.SPAWN_ORES).add(CosmereTags.Biomes.IS_ROSHAR);

		getBiomeBuilder(CosmereTags.Biomes.IS_ROSHAR).add(SurgebindingBiomes.ROSHAR_BIOME_KEY);
	}


	private void addContainsMetal()
	{

	}
}
