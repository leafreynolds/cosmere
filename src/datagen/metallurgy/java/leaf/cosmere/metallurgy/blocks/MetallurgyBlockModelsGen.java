package leaf.cosmere.metallurgy.blocks;

import leaf.cosmere.metallurgy.common.Metallurgy;
import leaf.cosmere.metallurgy.common.registries.MetallurgyBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MetallurgyBlockModelsGen extends BlockStateProvider {
	public MetallurgyBlockModelsGen(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
		super(packOutput, Metallurgy.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		// Metallurgy Workbench - simple block for now
		ModelFile workbenchModel = models().cubeAll("metallurgy_workbench",
				modLoc("block/metallurgy_workbench"));
		simpleBlock(MetallurgyBlocks.METALLURGY_WORKBENCH.getBlock(), workbenchModel);
	}
}
