/*
 * File updated ~ 30 - 4 - 2025 ~ Leaf
 */

package leaf.cosmere.tag;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.blocks.MetalBlock;
import leaf.cosmere.common.blocks.MetalOreBlock;
import leaf.cosmere.common.items.GodMetalAlloyNuggetItem;
import leaf.cosmere.common.items.MetalIngotItem;
import leaf.cosmere.common.registration.impl.BlockRegistryObject;
import leaf.cosmere.common.registration.impl.ItemRegistryObject;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.GameEventRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import leaf.cosmere.common.resource.ore.OreBlockType;
import leaf.cosmere.common.resource.ore.OreType;
import leaf.cosmere.common.util.CosmereEnumUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CosmereTagProvider extends BaseTagProvider
{

	public CosmereTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(packOutput, lookupProvider, Cosmere.MODID, existingFileHelper);
	}

	@Override
	protected List<IBlockProvider> getAllBlocks()
	{
		return BlocksRegistry.BLOCKS.getAllBlocks();
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
	}

	private void addItems()
	{
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{

			//if (metalType.hasMaterialItem())
			{
				// copper only needs nugget, don't tag ingot
				//if (metalType != Metals.MetalType.COPPER)
				{
					final ItemRegistryObject<MetalIngotItem> ingotRegObj = ItemsRegistry.METAL_INGOTS.get(metalType);
					if (ingotRegObj != null)
					{
						MetalIngotItem ingotItem = ingotRegObj.asItem();

						//don't need to tell the tag what each individual item is if they're tagged correctly
						//add(Tags.Items.INGOTS, ingotItem);

						// tell our ingots what their tags are.
						final TagKey<Item> metalIngotTag = metalType.getMetalIngotTag();
						addToTag(metalIngotTag, ingotItem);

						//tell the ingots that our ingot tags are part of them
						getItemBuilder(Tags.Items.INGOTS).add(metalIngotTag);
					}
				}

				//tell the nuggets that our nugget tags are part of them
				final TagKey<Item> metalNuggetTag = metalType.getMetalNuggetTag();
				getItemBuilder(Tags.Items.NUGGETS).add(metalNuggetTag);

				ItemRegistryObject<Item> nugRegObj = ItemsRegistry.METAL_NUGGETS.get(metalType);
				if (nugRegObj == null) nugRegObj = ItemsRegistry.GOD_METAL_NUGGETS.get(metalType);

				if (nugRegObj != null)
				{// tell the Nugget that our Nuggets are related
					Item nuggetItem = nugRegObj.asItem();

					//don't need to tell the tag what each individual item is if they're tagged correctly
					//add(Tags.Items.NUGGETS, nuggetItem);

					// tell our nuggets what their tags are.
					addToTag(metalNuggetTag, nuggetItem);
				}

				// Add the metal alloy nugget to the nugget tags
				if(!metalType.isGodMetal() && metalType.hasAssociatedManifestation())
				{
					for(Metals.MetalType godMetalType : new Metals.MetalType[] { Metals.MetalType.LERASIUM, Metals.MetalType.LERASATIUM})
					{
						final TagKey<Item> godMetalAlloyNuggetTag = godMetalType.getGodMetalAlloyNuggetTag(metalType);
						getItemBuilder(Tags.Items.NUGGETS).add(godMetalAlloyNuggetTag);

						ItemRegistryObject<GodMetalAlloyNuggetItem> alloyNugRegObj = ItemsRegistry.GOD_METAL_ALLOY_NUGGETS.get(godMetalType).get(metalType);
						if (alloyNugRegObj != null)
						{
							Item nuggetItem = alloyNugRegObj.asItem();
							addToTag(godMetalAlloyNuggetTag, nuggetItem);
						}
					}
				}
			}

			if (metalType.hasOre())
			{
				Item item = ItemsRegistry.METAL_RAW_ORE.get(metalType).asItem();
				final TagKey<Item> metalRawTag = metalType.getMetalRawTag();
				addToTag(metalRawTag, item);
			}

			if (metalType.isAlloy())
			{
				Item item = ItemsRegistry.METAL_RAW_BLEND.get(metalType).asItem();
				final TagKey<Item> metalBlendTag = metalType.getMetalBlendTag();
				addToTag(metalBlendTag, item);
			}
		}
	}

	private void addBlocks()
	{
		addToTag(BlockTags.NEEDS_STONE_TOOL, BlocksRegistry.METALWORKING_TABLE);
		addToHarvestTag(BlockTags.MINEABLE_WITH_AXE, BlocksRegistry.METALWORKING_TABLE);


		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (!metalType.hasMaterialItem() || metalType == Metals.MetalType.COPPER)
			{
				continue;
			}

			//put metal type tag on block
			final BlockRegistryObject<MetalBlock, BlockItem> metalBlock = BlocksRegistry.METAL_BLOCKS.get(metalType);

			final TagKey<Block> metalBlockTag = CosmereTags.Blocks.METAL_BLOCK_TAGS.get(metalType);
			addToHarvestTag(BlockTags.MINEABLE_WITH_PICKAXE, metalBlock);
			addToTag(BlockTags.NEEDS_IRON_TOOL, metalBlock);

			//put beacon tag on block
			getBlockBuilder(BlockTags.BEACON_BASE_BLOCKS).add(metalBlockTag);
		}

		for (OreType oreType : CosmereEnumUtils.ORE_TYPES)
		{
			final Metals.MetalType metalType = oreType.getMetalType();
			final OreBlockType oreBlockType = BlocksRegistry.METAL_ORE.get(oreType);

			final TagKey<Block> oreBlockTag = CosmereTags.Blocks.METAL_ORE_BLOCK_TAGS.get(metalType);
			final BlockRegistryObject<MetalOreBlock, BlockItem> stone = oreBlockType.stone();
			final BlockRegistryObject<MetalOreBlock, BlockItem> deepslate = oreBlockType.deepslate();
			addToTag(oreBlockTag, stone, deepslate);

			hasHarvestData(stone.getBlock());
			hasHarvestData(deepslate.getBlock());

			addToTag(BlockTags.NEEDS_STONE_TOOL, stone);
			addToTag(BlockTags.NEEDS_IRON_TOOL, deepslate);

			addToHarvestTag(BlockTags.MINEABLE_WITH_PICKAXE, stone, deepslate);
		}
	}

	private void addEntityTypes()
	{
		//getEntityTypeBuilder(CosmereTags.EntityTypes.CONTAINS_METAL).add(EntityType.IRON_GOLEM);
	}

	private void addGameEvents()
	{
		addToTag(GameEventTags.VIBRATIONS, GameEventRegistry.KINETIC_INVESTITURE);
		addToTag(GameEventTags.WARDEN_CAN_LISTEN, GameEventRegistry.KINETIC_INVESTITURE);
	}

	private void addBiomes()
	{
		getBiomeBuilder(CosmereTags.Biomes.SPAWN_ORES).add(BiomeTags.IS_OVERWORLD);
	}

	private void addStorageBlocks()
	{
		final IntrinsicCosmereTagBuilder<Item> itemBuilder = getItemBuilder(Tags.Items.STORAGE_BLOCKS);
		final IntrinsicCosmereTagBuilder<Block> blockBuilder = getBlockBuilder(Tags.Blocks.STORAGE_BLOCKS);

		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (!metalType.hasMaterialItem() || metalType == Metals.MetalType.COPPER)
			{
				continue;
			}

			final TagKey<Item> storageBlockItemTag = CosmereTags.Items.METAL_BLOCK_ITEM_TAGS.get(metalType);
			final TagKey<Block> storageBlockTag = CosmereTags.Blocks.METAL_BLOCK_TAGS.get(metalType);
			final BlockRegistryObject<MetalBlock, BlockItem> blockRegistryObject = BlocksRegistry.METAL_BLOCKS.get(metalType);

			addToTags(storageBlockItemTag, storageBlockTag, blockRegistryObject);

			itemBuilder.add(storageBlockItemTag);
			blockBuilder.add(storageBlockTag);
		}
	}

	private void addContainsMetal()
	{
		final IntrinsicCosmereTagBuilder<Item> itemTagBuilder = getItemBuilder(CosmereTags.Items.CONTAINS_METAL);
		final IntrinsicCosmereTagBuilder<Block> blockTagBuilder = getBlockBuilder(CosmereTags.Blocks.CONTAINS_METAL);
		final IntrinsicCosmereTagBuilder<EntityType<?>> entityTagBuilder = getEntityTypeBuilder(CosmereTags.EntityTypes.CONTAINS_METAL);

		itemTagBuilder.addOptionalTag(CosmereTags.Items.METAL_SPIKE);

		//Our metals
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			if (metalType == Metals.MetalType.ALUMINUM)
			{
				//skip aluminum
				continue;
			}

			//Items
			final TagKey<Item> metalRawTag = CosmereTags.Items.METAL_RAW_TAGS.get(metalType);
			final TagKey<Item> metalBlendTag = CosmereTags.Items.METAL_DUST_TAGS.get(metalType);
			final TagKey<Item> metalNuggetTag = CosmereTags.Items.METAL_NUGGET_TAGS.get(metalType);
			final TagKey<Item> metalIngotTag = CosmereTags.Items.METAL_INGOT_TAGS.get(metalType);
			final TagKey<Item> storageItemBlockTag = CosmereTags.Items.METAL_BLOCK_ITEM_TAGS.get(metalType);
			//Blocks
			final TagKey<Block> oreBlockTag = CosmereTags.Blocks.METAL_ORE_BLOCK_TAGS.get(metalType);
			final TagKey<Block> storageBlockTag = CosmereTags.Blocks.METAL_BLOCK_TAGS.get(metalType);

			if (metalType.hasOre()
					|| metalType == Metals.MetalType.IRON
					|| metalType == Metals.MetalType.COPPER
					|| metalType == Metals.MetalType.GOLD)
			{
				itemTagBuilder.add(metalRawTag);
				blockTagBuilder.add(oreBlockTag);
			}

			if (metalType.isAlloy())
			{
				itemTagBuilder.add(metalBlendTag);
			}

			//all else is guaranteed to have stuff in it
			itemTagBuilder.add(metalNuggetTag);
			itemTagBuilder.add(metalIngotTag);
			itemTagBuilder.add(storageItemBlockTag);
			blockTagBuilder.add(storageBlockTag);
		}

		addCopperBlockVariations(itemTagBuilder, blockTagBuilder);

		//entities
		entityTagBuilder.add(EntityType.IRON_GOLEM);
		entityTagBuilder.add(EntityType.MINECART);
		entityTagBuilder.add(EntityType.CHEST_MINECART);
		entityTagBuilder.add(EntityType.FURNACE_MINECART);
		entityTagBuilder.add(EntityType.HOPPER_MINECART);
		entityTagBuilder.add(EntityType.TNT_MINECART);
	}

	private void addCopperBlockVariations(IntrinsicCosmereTagBuilder<Item> itemTagBuilder, IntrinsicCosmereTagBuilder<Block> blockTagBuilder)
	{
		itemTagBuilder.add(Items.EXPOSED_COPPER);
		itemTagBuilder.add(Items.EXPOSED_CUT_COPPER);
		itemTagBuilder.add(Items.EXPOSED_CUT_COPPER_SLAB);
		itemTagBuilder.add(Items.EXPOSED_CUT_COPPER_STAIRS);
		itemTagBuilder.add(Items.WAXED_EXPOSED_COPPER);
		itemTagBuilder.add(Items.WAXED_EXPOSED_CUT_COPPER);
		itemTagBuilder.add(Items.WAXED_EXPOSED_CUT_COPPER_SLAB);
		itemTagBuilder.add(Items.WAXED_EXPOSED_CUT_COPPER_STAIRS);

		itemTagBuilder.add(Items.WEATHERED_COPPER);
		itemTagBuilder.add(Items.WEATHERED_CUT_COPPER);
		itemTagBuilder.add(Items.WEATHERED_CUT_COPPER_SLAB);
		itemTagBuilder.add(Items.WEATHERED_CUT_COPPER_STAIRS);
		itemTagBuilder.add(Items.WAXED_WEATHERED_COPPER);
		itemTagBuilder.add(Items.WAXED_WEATHERED_CUT_COPPER);
		itemTagBuilder.add(Items.WAXED_WEATHERED_CUT_COPPER_SLAB);
		itemTagBuilder.add(Items.WAXED_WEATHERED_CUT_COPPER_STAIRS);

		itemTagBuilder.add(Items.OXIDIZED_COPPER);
		itemTagBuilder.add(Items.OXIDIZED_CUT_COPPER);
		itemTagBuilder.add(Items.OXIDIZED_CUT_COPPER_SLAB);
		itemTagBuilder.add(Items.OXIDIZED_CUT_COPPER_STAIRS);
		itemTagBuilder.add(Items.WAXED_OXIDIZED_COPPER);
		itemTagBuilder.add(Items.WAXED_OXIDIZED_CUT_COPPER);
		itemTagBuilder.add(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB);
		itemTagBuilder.add(Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS);

		itemTagBuilder.add(Items.WAXED_COPPER_BLOCK);
		itemTagBuilder.add(Items.WAXED_CUT_COPPER);
		itemTagBuilder.add(Items.WAXED_CUT_COPPER_SLAB);
		itemTagBuilder.add(Items.WAXED_CUT_COPPER_STAIRS);

		blockTagBuilder.add(Blocks.EXPOSED_COPPER);
		blockTagBuilder.add(Blocks.EXPOSED_CUT_COPPER);
		blockTagBuilder.add(Blocks.EXPOSED_CUT_COPPER_SLAB);
		blockTagBuilder.add(Blocks.EXPOSED_CUT_COPPER_STAIRS);
		blockTagBuilder.add(Blocks.WAXED_EXPOSED_COPPER);
		blockTagBuilder.add(Blocks.WAXED_EXPOSED_CUT_COPPER);
		blockTagBuilder.add(Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB);
		blockTagBuilder.add(Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS);

		blockTagBuilder.add(Blocks.WEATHERED_COPPER);
		blockTagBuilder.add(Blocks.WEATHERED_CUT_COPPER);
		blockTagBuilder.add(Blocks.WEATHERED_CUT_COPPER_SLAB);
		blockTagBuilder.add(Blocks.WEATHERED_CUT_COPPER_STAIRS);
		blockTagBuilder.add(Blocks.WAXED_WEATHERED_COPPER);
		blockTagBuilder.add(Blocks.WAXED_WEATHERED_CUT_COPPER);
		blockTagBuilder.add(Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB);
		blockTagBuilder.add(Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS);

		blockTagBuilder.add(Blocks.OXIDIZED_COPPER);
		blockTagBuilder.add(Blocks.OXIDIZED_CUT_COPPER);
		blockTagBuilder.add(Blocks.OXIDIZED_CUT_COPPER_SLAB);
		blockTagBuilder.add(Blocks.OXIDIZED_CUT_COPPER_STAIRS);
		blockTagBuilder.add(Blocks.WAXED_OXIDIZED_COPPER);
		blockTagBuilder.add(Blocks.WAXED_OXIDIZED_CUT_COPPER);
		blockTagBuilder.add(Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB);
		blockTagBuilder.add(Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);

		blockTagBuilder.add(Blocks.WAXED_COPPER_BLOCK);
		blockTagBuilder.add(Blocks.WAXED_CUT_COPPER);
		blockTagBuilder.add(Blocks.WAXED_CUT_COPPER_SLAB);
		blockTagBuilder.add(Blocks.WAXED_CUT_COPPER_STAIRS);
	}
}