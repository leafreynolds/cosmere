/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.blocks.*;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class SurgebindingBlockModelsGen extends BlockStateProvider
{
	public SurgebindingBlockModelsGen(PackOutput generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, Surgebinding.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		for (IBlockProvider itemRegistryObject : SurgebindingBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			if (block instanceof GemBlock)
			{
				ModelFile blockModel = models().withExistingParent("gem_block", Cosmere.rl("block/shapes/cube_all_tinted"))
						.texture("all", Surgebinding.rl("block/gem_block"));

				simpleBlock(block, blockModel);
				continue;
			}
			else if (block instanceof GemOreBlock)
			{
				//Special thanks to @Random & @sciwhiz12  on discord who helped me get these running
				//To get the overlay working, you need to tell the blocks they have transparency, which I've donne in the ClientSetup script.
				final boolean deepslate = RegistryHelper.get(block).getPath().contains("deepslate");

				final String stoneType = deepslate ? "block/gem_ore_block_deepslate" : "block/gem_ore_block";
				final String stoneFileName = deepslate ? "gem_ore_block_deepslate" : "gem_ore_block";

				ModelFile blockModel = models().withExistingParent(stoneFileName, Cosmere.rl("block/shapes/cube_with_tint_overlay"))
						.texture("all", Surgebinding.rl(stoneType))
						.texture("overlay", Surgebinding.rl("block/gem_ore_block_tint_overlay"));

				simpleBlock(block, blockModel);
				continue;
			}
			else if (block instanceof LavisPolypBlock || block instanceof PrickletacBlock || block instanceof RockbudVariantBlock || block instanceof VinebudBlock)
			{
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