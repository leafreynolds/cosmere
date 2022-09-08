/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.blocks;

import com.google.common.collect.ImmutableList;
import leaf.cosmere.Cosmere;
import leaf.cosmere.blocks.GemBlock;
import leaf.cosmere.blocks.GemOreBlock;
import leaf.cosmere.blocks.MetalBlock;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.constants.Roshar;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsGen extends BlockTagsProvider
{
	public BlockTagsGen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
	{
		super(generatorIn, Cosmere.MODID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{

		for (Roshar.Gemstone gemstone : Roshar.Gemstone.values())
		{
			final GemBlock gemBlock = BlocksRegistry.GEM_BLOCKS.get(gemstone).get();
			final GemOreBlock gemOre = BlocksRegistry.GEM_ORE.get(gemstone).get();
			final GemOreBlock gemOreDeepslate = BlocksRegistry.GEM_ORE_DEEPSLATE.get(gemstone).get();

			var list = ImmutableList.of(gemBlock, gemOre, gemOreDeepslate);

			for (Block block : list)
			{
				add(TagsRegistry.Blocks.DRAGON_PROOF, block);
				//add(TagsRegistry.Blocks.WITHER_PROOF, BlocksRegistry.GEM_BLOCKS.get(gemstone).get());

				add(BlockTags.MINEABLE_WITH_PICKAXE, block);
				add(BlockTags.NEEDS_IRON_TOOL, block);
			}


			add(BlockTags.BEACON_BASE_BLOCKS, gemBlock);
			add(Tags.Blocks.STORAGE_BLOCKS, gemBlock);


			add(TagsRegistry.Blocks.GEM_ORE_BLOCK_TAGS.get(gemstone), gemOre);
			add(TagsRegistry.Blocks.GEM_ORE_BLOCK_TAGS.get(gemstone), gemOreDeepslate);

			add(TagsRegistry.Blocks.GEM_BLOCK_TAGS.get(gemstone), gemBlock);
		}

		add(BlockTags.NEEDS_STONE_TOOL,BlocksRegistry.METALWORKING_TABLE.get());
		add(BlockTags.MINEABLE_WITH_AXE,BlocksRegistry.METALWORKING_TABLE.get());


		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasMaterialItem())
			{
				continue;
			}

			if (metalType.hasOre())
			{
				final DropExperienceBlock oreBlock = metalType.getOreBlock();
				add(TagsRegistry.Blocks.METAL_ORE_BLOCK_TAGS.get(metalType), oreBlock);
				add(BlockTags.MINEABLE_WITH_PICKAXE, oreBlock);
				add(BlockTags.NEEDS_STONE_TOOL, oreBlock);

				final DropExperienceBlock oreDeepslateBlock = metalType.getDeepslateOreBlock();
				add(TagsRegistry.Blocks.METAL_ORE_BLOCK_TAGS.get(metalType), oreDeepslateBlock);
				add(BlockTags.MINEABLE_WITH_PICKAXE, oreDeepslateBlock);
				add(BlockTags.NEEDS_IRON_TOOL, oreDeepslateBlock);
			}

			//put metal type tag on block
			MetalBlock metalBlock = metalType.getBlock();

			add(TagsRegistry.Blocks.METAL_BLOCK_TAGS.get(metalType), metalBlock);
			add(BlockTags.MINEABLE_WITH_PICKAXE, metalBlock);
			add(BlockTags.NEEDS_IRON_TOOL, metalBlock);
			//put storage block tag on block, though I don't know what it is for.
			add(Tags.Blocks.STORAGE_BLOCKS, metalBlock);
			//put beacon tag on block
			add(BlockTags.BEACON_BASE_BLOCKS, metalBlock);
		}
	}

	public void add(TagKey<Block> branch, Block block)
	{
		this.tag(branch).add(block);
	}

	public void add(TagKey<Block> branch, Block... block)
	{
		this.tag(branch).add(block);
	}
}
