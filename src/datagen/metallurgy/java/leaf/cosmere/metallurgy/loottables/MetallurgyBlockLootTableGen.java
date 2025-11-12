package leaf.cosmere.metallurgy.loottables;

import leaf.cosmere.loottables.BaseBlockLootTables;
import leaf.cosmere.metallurgy.common.registries.MetallurgyBlocks;
import net.minecraft.world.level.block.Block;

import java.util.stream.Collectors;

public class MetallurgyBlockLootTableGen extends BaseBlockLootTables {
	@Override
	protected void generate() {
		// Metallurgy Workbench drops itself
		dropSelf(MetallurgyBlocks.METALLURGY_WORKBENCH.getBlock());
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return MetallurgyBlocks.BLOCKS.getAllBlocks()
				.stream()
				.map(provider -> provider.getBlock())
				.collect(Collectors.toList());
	}
}
