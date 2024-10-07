/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.loottables;

import leaf.cosmere.loottables.BaseLootProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class SandmasteryLootTableGen extends BaseLootProvider
{
	public SandmasteryLootTableGen(PackOutput packOutput)
	{
		super(packOutput, List.of(
				new SubProviderEntry(SandmasteryBlockLootTableGen::new, LootContextParamSets.BLOCK)
				//,new SubProviderEntry(ExampleEntityLootTableGen::new, LootContextParamSets.ENTITY)
		));
	}
}
