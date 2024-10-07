/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.loottables;

import leaf.cosmere.loottables.BaseLootProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class SurgebindingLootTableGen extends BaseLootProvider
{
	public SurgebindingLootTableGen(PackOutput output)
	{
		super(output, List.of(
				new SubProviderEntry(SurgebindingBlockLootTableGen::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(SurgebindingEntityLootTableGen::new, LootContextParamSets.ENTITY)
		));
	}
}
