package leaf.cosmere.metallurgy.loottables;

import leaf.cosmere.loottables.BaseLootProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class MetallurgyLootTableGen extends BaseLootProvider {
	public MetallurgyLootTableGen(PackOutput output) {
		super(output, List.of(
				new SubProviderEntry(MetallurgyBlockLootTableGen::new, LootContextParamSets.BLOCK)
		));
	}
}
