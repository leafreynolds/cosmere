/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.blocks;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SandmasteryBlockTagsGen extends BlockTagsProvider
{
	public SandmasteryBlockTagsGen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
	{
		super(generatorIn, Cosmere.MODID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		add(BlockTags.MINEABLE_WITH_SHOVEL, SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SAND.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock());
		add(BlockTags.SAND, SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_BLACK_SAND.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock(), SandmasteryBlocks.TALDAIN_WHITE_SAND.getBlock());
		add(BlockTags.MINEABLE_WITH_PICKAXE, SandmasteryBlocks.SAND_JAR_BLOCK.getBlock());
		add(BlockTags.MINEABLE_WITH_AXE, SandmasteryBlocks.SAND_SPREADING_TUB_BLOCK.getBlock());

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
