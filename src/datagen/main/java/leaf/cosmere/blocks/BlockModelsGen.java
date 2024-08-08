/*
 * File updated ~ 6 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.blocks;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.blocks.MetalBlock;
import leaf.cosmere.common.blocks.MetalOreBlock;
import leaf.cosmere.common.blocks.MetalworkingTableBlock;
import leaf.cosmere.common.registry.BlocksRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class BlockModelsGen extends BlockStateProvider
{
	public BlockModelsGen(PackOutput packOutput, ExistingFileHelper existingFileHelper)
	{
		super(packOutput, Cosmere.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		for (IBlockProvider itemRegistryObject : BlocksRegistry.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			if (block instanceof MetalBlock)
			{
				//ModelFile blockModel = models().cubeAll(getPath(itemRegistryObject), new ResourceLocation(Cosmere.MODID, "block/metal_block"));

				//thank you botania! <3 tinting is awesome
				ModelFile blockModel = models().withExistingParent("metal_block", Cosmere.rl("block/shapes/cube_all_tinted"))
						.texture("all", Cosmere.rl("block/metal_block"));

				simpleBlock(block, blockModel);
				continue;
			}
			else if (block instanceof MetalworkingTableBlock)
			{
				ModelFile blockModel = models().withExistingParent("metalworking_table", new ResourceLocation("block/cube"))
						.texture("particle", Cosmere.rl("block/metalworking_table_front"))
						.texture("north", Cosmere.rl("block/metalworking_table_front"))
						.texture("south", Cosmere.rl("block/metalworking_table_front"))
						.texture("east", Cosmere.rl("block/metalworking_table_side"))
						.texture("west", Cosmere.rl("block/metalworking_table_side"))
						.texture("up", Cosmere.rl("block/metalworking_table_top"))
						.texture("down", Cosmere.rl("block/metalworking_table_bottom"));
				simpleBlock(block, blockModel);
				continue;
			}
			else if (block instanceof MetalOreBlock)
			{
				//Special thanks to @Random & @sciwhiz12  on discord who helped me get these running
				//To get the overlay working, you need to tell the blocks they have transparency, which I've donne in the ClientSetup script.
				final boolean deepslate = RegistryHelper.get(block).getPath().contains("deepslate");

				final String stoneType = deepslate ? "block/ore_block_deepslate" : "block/ore_block";
				final String stoneFileName = deepslate ? "ore_block_deepslate" : "ore_block";

				ModelFile blockModel = models().withExistingParent(stoneFileName, Cosmere.rl("block/shapes/cube_with_tint_overlay"))
						.texture("all", Cosmere.rl(stoneType))
						.texture("overlay", Cosmere.rl("block/ore_block_tint_overlay"));

				simpleBlock(block, blockModel);
				continue;
			}
			simpleBlock(itemRegistryObject.getBlock());
		}

	}

	public void simpleBlock(Supplier<? extends Block> blockSupplier)
	{
		simpleBlock(blockSupplier.get());
	}


	@Override
	public void simpleBlock(Block block, ModelFile model)
	{
		super.simpleBlock(block, model);
		this.simpleBlockItem(block, model);
	}
}