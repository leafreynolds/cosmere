/*
 * File updated ~ 12 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.tag;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.blocks.MetalBlock;
import leaf.cosmere.common.blocks.MetalOreBlock;
import leaf.cosmere.common.items.MetalIngotItem;
import leaf.cosmere.common.registration.impl.BlockRegistryObject;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.GameEventRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CosmereTagProvider extends BaseTagProvider
{

	public CosmereTagProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(gen, Cosmere.MODID, existingFileHelper);
	}

	@Override
	protected List<IBlockProvider> getAllBlocks()
	{
		return BlocksRegistry.BLOCKS.getAllBlocks();
	}

	@Override
	protected void registerTags()
	{
		addItems();
		addBlocks();
		addStorageBlocks();
		addEntityTypes();
		addGameEvents();

		addContainsMetal();
	}

	private void addItems()
	{
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{

			if (metalType.hasMaterialItem())
			{
				MetalIngotItem ingotItem = ItemsRegistry.METAL_INGOTS.get(metalType).asItem();

				//don't need to tell the tag what each individual item is if they're tagged correctly
				//add(Tags.Items.INGOTS, ingotItem);

				// tell our ingots what their tags are.
				final TagKey<Item> metalIngotTag = metalType.getMetalIngotTag();
				addToTag(metalIngotTag, ingotItem);

				//tell the ingots that our ingot tags are part of them
				getItemBuilder(Tags.Items.INGOTS).add(metalIngotTag);

				//tell the nuggets that our nugget tags are part of them
				final TagKey<Item> metalNuggetTag = metalType.getMetalNuggetTag();
				getItemBuilder(Tags.Items.NUGGETS).add(metalNuggetTag);

				// tell the Nugget that our Nuggets are related
				Item nuggetItem = ItemsRegistry.METAL_NUGGETS.get(metalType).asItem();

				//don't need to tell the tag what each individual item is if they're tagged correctly
				//add(Tags.Items.NUGGETS, nuggetItem);

				// tell our nuggets what their tags are.
				addToTag(metalNuggetTag, nuggetItem);

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


		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasMaterialItem())
			{
				continue;
			}

			if (metalType.hasOre())
			{
				final BlockRegistryObject<MetalOreBlock, BlockItem> oreBlock = BlocksRegistry.METAL_ORE.get(metalType);
				final BlockRegistryObject<MetalOreBlock, BlockItem> oreDeepslateBlock = BlocksRegistry.METAL_ORE_DEEPSLATE.get(metalType);

				final TagKey<Block> oreBlockTag = CosmereTags.Blocks.METAL_ORE_BLOCK_TAGS.get(metalType);
				addToTag(oreBlockTag, oreBlock, oreDeepslateBlock);

				addToTag(BlockTags.NEEDS_STONE_TOOL, oreBlock);
				addToTag(BlockTags.NEEDS_IRON_TOOL, oreDeepslateBlock);

				addToHarvestTag(BlockTags.MINEABLE_WITH_PICKAXE, oreBlock, oreDeepslateBlock);
			}

			//put metal type tag on block
			final BlockRegistryObject<MetalBlock, BlockItem> metalBlock = BlocksRegistry.METAL_BLOCKS.get(metalType);

			final TagKey<Block> metalBlockTag = CosmereTags.Blocks.METAL_BLOCK_TAGS.get(metalType);
			addToHarvestTag(BlockTags.MINEABLE_WITH_PICKAXE, metalBlock);
			addToTag(BlockTags.NEEDS_IRON_TOOL, metalBlock);

			//put beacon tag on block
			getBlockBuilder(BlockTags.BEACON_BASE_BLOCKS).add(metalBlockTag);
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

	private void addStorageBlocks()
	{
		final ForgeRegistryTagBuilder<Item> itemBuilder = getItemBuilder(Tags.Items.STORAGE_BLOCKS);
		final ForgeRegistryTagBuilder<Block> blockBuilder = getBlockBuilder(Tags.Blocks.STORAGE_BLOCKS);

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasMaterialItem())
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
		final ForgeRegistryTagBuilder<Item> itemTagBuilder = getItemBuilder(CosmereTags.Items.CONTAINS_METAL);
		final ForgeRegistryTagBuilder<Block> blockTagBuilder = getBlockBuilder(CosmereTags.Blocks.CONTAINS_METAL);
		final ForgeRegistryTagBuilder<EntityType<?>> entityTagBuilder = getEntityTypeBuilder(CosmereTags.EntityTypes.CONTAINS_METAL);

		itemTagBuilder.addOptionalTag(CosmereTags.Items.METAL_SPIKE);

		//Our metals
		for (Metals.MetalType metalType : Metals.MetalType.values())
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

		//entities
		entityTagBuilder.add(EntityType.IRON_GOLEM);
	}
}