/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.blocks;

import leaf.cosmere.Cosmere;
import leaf.cosmere.blocks.MetalBlock;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.registry.TagsRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsGen extends BlockTagsProvider
{
    public BlockTagsGen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
    {
        super(generatorIn, Cosmere.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags()
    {
        add(TagsRegistry.Blocks.DRAGON_PROOF, BlocksRegistry.GEM_BLOCK.get());
        add(TagsRegistry.Blocks.WITHER_PROOF, BlocksRegistry.GEM_BLOCK.get());
        add(BlockTags.BEACON_BASE_BLOCKS, BlocksRegistry.GEM_BLOCK.get());

        for (Metals.MetalType metalType : Metals.MetalType.values())
        {
            if (!metalType.hasMaterialItem())
                continue;

            //put metal type tag on block
            MetalBlock metalBlock = metalType.getBlock();

            add(TagsRegistry.Blocks.METAL_BLOCK_TAGS.get(metalType), metalBlock);
            //put storage block tag on block, though I don't know what it is for.
            add(Tags.Blocks.STORAGE_BLOCKS, metalBlock);
            //put beacon tag on block
            add(BlockTags.BEACON_BASE_BLOCKS, metalBlock);
        }
    }

    public void add(ITag.INamedTag<Block> branch, Block block)
    {
        this.getOrCreateBuilder(branch).add(block);
    }

    public void add(ITag.INamedTag<Block> branch, Block... block)
    {
        this.getOrCreateBuilder(branch).add(block);
    }
}
