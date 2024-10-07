/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.loottables;

import leaf.cosmere.loottables.BaseLootProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class ToolsLootTableGen extends BaseLootProvider
{
	public ToolsLootTableGen(PackOutput packOutput)
	{
		super(packOutput, List.of(
				new SubProviderEntry(ToolsBlockLootTableGen::new, LootContextParamSets.BLOCK)
				//,new SubProviderEntry(ExampleEntityLootTableGen::new, LootContextParamSets.ENTITY)
		));
	}
}
