/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.blocks;

import com.google.common.collect.ImmutableList;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.blocks.GemBlock;
import leaf.cosmere.surgebinding.common.blocks.GemOreBlock;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SurgebindingBlockTagsGen extends BlockTagsProvider
{
	public SurgebindingBlockTagsGen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
	{
		super(generatorIn, Surgebinding.MODID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{

		for (Roshar.Gemstone gemstone : Roshar.Gemstone.values())
		{
			final GemBlock gemBlock = SurgebindingBlocks.GEM_BLOCKS.get(gemstone).getBlock();
			final GemOreBlock gemOre = SurgebindingBlocks.GEM_ORE.get(gemstone).getBlock();
			final GemOreBlock gemOreDeepslate = SurgebindingBlocks.GEM_ORE_DEEPSLATE.get(gemstone).getBlock();

			var list = ImmutableList.of(gemBlock, gemOre, gemOreDeepslate);

			for (Block block : list)
			{
				add(CosmereTags.Blocks.DRAGON_PROOF, block);
				//add(TagsRegistry.Blocks.WITHER_PROOF, BlocksRegistry.GEM_BLOCKS.get(gemstone).get());

				add(BlockTags.MINEABLE_WITH_PICKAXE, block);
				add(BlockTags.NEEDS_IRON_TOOL, block);
			}


			add(BlockTags.BEACON_BASE_BLOCKS, gemBlock);
			add(Tags.Blocks.STORAGE_BLOCKS, gemBlock);


			add(CosmereTags.Blocks.GEM_ORE_BLOCK_TAGS.get(gemstone), gemOre);
			add(CosmereTags.Blocks.GEM_ORE_BLOCK_TAGS.get(gemstone), gemOreDeepslate);

			add(CosmereTags.Blocks.GEM_BLOCK_TAGS.get(gemstone), gemBlock);
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
