/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.blocks;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.blocks.MetalBlock;
import leaf.cosmere.common.registry.BlocksRegistry;
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
		add(BlockTags.NEEDS_STONE_TOOL, BlocksRegistry.METALWORKING_TABLE.getBlock());
		add(BlockTags.MINEABLE_WITH_AXE, BlocksRegistry.METALWORKING_TABLE.getBlock());

		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			if (!metalType.hasMaterialItem())
			{
				continue;
			}

			if (metalType.hasOre())
			{
				final DropExperienceBlock oreBlock = BlocksRegistry.METAL_ORE.get(metalType).getBlock();
				add(CosmereTags.Blocks.METAL_ORE_BLOCK_TAGS.get(metalType), oreBlock);
				add(BlockTags.MINEABLE_WITH_PICKAXE, oreBlock);
				add(BlockTags.NEEDS_STONE_TOOL, oreBlock);

				final DropExperienceBlock oreDeepslateBlock = BlocksRegistry.METAL_ORE_DEEPSLATE.get(metalType).getBlock();
				add(CosmereTags.Blocks.METAL_ORE_BLOCK_TAGS.get(metalType), oreDeepslateBlock);
				add(BlockTags.MINEABLE_WITH_PICKAXE, oreDeepslateBlock);
				add(BlockTags.NEEDS_IRON_TOOL, oreDeepslateBlock);
			}

			//put metal type tag on block
			final MetalBlock metalBlock = BlocksRegistry.METAL_BLOCKS.get(metalType).getBlock();

			add(CosmereTags.Blocks.METAL_BLOCK_TAGS.get(metalType), metalBlock);
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
