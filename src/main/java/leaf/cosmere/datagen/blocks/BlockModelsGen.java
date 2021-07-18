/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.blocks;

import leaf.cosmere.Cosmere;
import leaf.cosmere.blocks.MetalBlock;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import leaf.cosmere.registry.BlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class BlockModelsGen extends BlockStateProvider
{
    public BlockModelsGen(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, Cosmere.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        for (RegistryObject<Block> itemRegistryObject : BlocksRegistry.BLOCKS.getEntries())
        {
            if (itemRegistryObject.get() instanceof MetalBlock)
            {
                //ModelFile blockModel = models().cubeAll(getPath(itemRegistryObject), new ResourceLocation(Cosmere.MODID, "block/metal_block"));

                //thank you botania! <3 tinting is awesome
                ModelFile blockModel = models().withExistingParent("metal_block", ResourceLocationHelper.prefix("block/shapes/cube_all_tinted"))
                        .texture("all", ResourceLocationHelper.prefix("block/metal_block"));

                simpleBlock(itemRegistryObject.get(), blockModel);
                continue;
            }
            else if (itemRegistryObject.get() instanceof OreBlock)
            {
                //Special thanks to @Random & @sciwhiz12  on discord who helped me get these running
                //To get the overlay working, you need to tell the blocks they have transparency, which I've donne in the ClientSetup script.
                ModelFile blockModel = models().withExistingParent("ore_block", ResourceLocationHelper.prefix("block/shapes/cube_with_tint_overlay"))
                        .texture("all", ResourceLocationHelper.prefix("block/ore_block"))
                        .texture("overlay",ResourceLocationHelper.prefix("block/ore_block_tint_overlay"));

                simpleBlock(itemRegistryObject.get(), blockModel);
                continue;
            }

            simpleBlock(itemRegistryObject);
        }

    }

    public void simpleBlock(Supplier<? extends Block> blockSupplier)
    {
        simpleBlock(blockSupplier.get());
    }

    public String getPath(Supplier<? extends Block> blockSupplier)
    {
        ResourceLocation location = blockSupplier.get().getRegistryName();
        return location.getPath();
    }

    @Override
    public void simpleBlock(Block block, ModelFile model)
    {
        super.simpleBlock(block, model);
        this.simpleBlockItem(block, model);
    }
}