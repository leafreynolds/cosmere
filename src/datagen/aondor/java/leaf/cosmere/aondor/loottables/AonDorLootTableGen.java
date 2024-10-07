/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.aondor.loottables;

import leaf.cosmere.loottables.BaseLootProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class AonDorLootTableGen extends BaseLootProvider
{
	public AonDorLootTableGen(PackOutput packOutput)
	{
		super(packOutput, List.of(
				new SubProviderEntry(AonDorBlockLootTableGen::new, LootContextParamSets.BLOCK)
		));
	}
}
