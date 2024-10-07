/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.awakening.loottables;

import leaf.cosmere.loottables.BaseLootProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class AwakeningLootTableGen extends BaseLootProvider
{
	public AwakeningLootTableGen(PackOutput packOutput)
	{
		super(packOutput, List.of(
				new SubProviderEntry(AwakeningBlockLootTableGen::new, LootContextParamSets.BLOCK)
				//,new SubProviderEntry(ExampleEntityLootTableGen::new, LootContextParamSets.ENTITY)
		));
	}
}
