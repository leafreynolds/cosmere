/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.blocks;

import leaf.cosmere.Cosmere;
import leaf.cosmere.blocks.*;
import leaf.cosmere.registry.BlocksRegistry;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

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
			final Block block = itemRegistryObject.get();
			if (block instanceof MetalBlock)
			{
				//ModelFile blockModel = models().cubeAll(getPath(itemRegistryObject), new ResourceLocation(Cosmere.MODID, "block/metal_block"));

				//thank you botania! <3 tinting is awesome
				ModelFile blockModel = models().withExistingParent("metal_block", ResourceLocationHelper.prefix("block/shapes/cube_all_tinted"))
						.texture("all", ResourceLocationHelper.prefix("block/metal_block"));

				simpleBlock(block, blockModel);
				continue;
			}
			else if (block instanceof GemBlock)
			{
				ModelFile blockModel = models().withExistingParent("gem_block", ResourceLocationHelper.prefix("block/shapes/cube_all_tinted"))
						.texture("all", ResourceLocationHelper.prefix("block/gem_block"));

				simpleBlock(block, blockModel);
				continue;
			}
			else if (block instanceof MetalworkingTableBlock)
			{
				ModelFile blockModel = models().withExistingParent("metalworking_table", new ResourceLocation("block/cube"))
						.texture("particle", ResourceLocationHelper.prefix("block/metalworking_table_front"))
						.texture("north", ResourceLocationHelper.prefix("block/metalworking_table_front"))
						.texture("south", ResourceLocationHelper.prefix("block/metalworking_table_front"))
						.texture("east", ResourceLocationHelper.prefix("block/metalworking_table_side"))
						.texture("west", ResourceLocationHelper.prefix("block/metalworking_table_side"))
						.texture("up", ResourceLocationHelper.prefix("block/metalworking_table_top"))
						.texture("down", ResourceLocationHelper.prefix("block/metalworking_table_bottom"));
				simpleBlock(block, blockModel);
				continue;
			}
			else if (block instanceof MetalOreBlock)
			{
				//Special thanks to @Random & @sciwhiz12  on discord who helped me get these running
				//To get the overlay working, you need to tell the blocks they have transparency, which I've donne in the ClientSetup script.
				final boolean deepslate = ResourceLocationHelper.get(block).getPath().contains("deepslate");

				final String stoneType = deepslate ? "block/ore_block_deepslate" : "block/ore_block";
				final String stoneFileName = deepslate ? "ore_block_deepslate" : "ore_block";

				ModelFile blockModel = models().withExistingParent(stoneFileName, ResourceLocationHelper.prefix("block/shapes/cube_with_tint_overlay"))
						.texture("all", ResourceLocationHelper.prefix(stoneType))
						.texture("overlay", ResourceLocationHelper.prefix("block/ore_block_tint_overlay"));

				simpleBlock(block, blockModel);
				continue;
			}
			else if (block instanceof GemOreBlock)
			{
				//Special thanks to @Random & @sciwhiz12  on discord who helped me get these running
				//To get the overlay working, you need to tell the blocks they have transparency, which I've donne in the ClientSetup script.
				final boolean deepslate = ResourceLocationHelper.get(block).getPath().contains("deepslate");

				final String stoneType = deepslate ? "block/gem_ore_block_deepslate" : "block/gem_ore_block";
				final String stoneFileName = deepslate ? "gem_ore_block_deepslate" : "gem_ore_block";

				ModelFile blockModel = models().withExistingParent(stoneFileName, ResourceLocationHelper.prefix("block/shapes/cube_with_tint_overlay"))
						.texture("all", ResourceLocationHelper.prefix(stoneType))
						.texture("overlay", ResourceLocationHelper.prefix("block/gem_ore_block_tint_overlay"));

				simpleBlock(block, blockModel);
				continue;
			}

			simpleBlock(itemRegistryObject);
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