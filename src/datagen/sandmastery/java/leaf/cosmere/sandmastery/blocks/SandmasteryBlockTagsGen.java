package leaf.cosmere.sandmastery.blocks;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.blocks.MetalBlock;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraftforge.common.Tags;
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
        add(BlockTags.MINEABLE_WITH_SHOVEL, SandmasteryBlocksRegistry.TALDAIN_SAND_LAYER.getBlock(), SandmasteryBlocksRegistry.TALDAIN_SAND.getBlock());
        add(BlockTags.SAND, SandmasteryBlocksRegistry.TALDAIN_SAND_LAYER.getBlock(), SandmasteryBlocksRegistry.TALDAIN_SAND.getBlock());
        add(BlockTags.MINEABLE_WITH_PICKAXE, SandmasteryBlocksRegistry.SAND_JAR_BLOCK.getBlock());
        add(BlockTags.MINEABLE_WITH_AXE, SandmasteryBlocksRegistry.SAND_SPREADING_TUB_BLOCK.getBlock());

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
