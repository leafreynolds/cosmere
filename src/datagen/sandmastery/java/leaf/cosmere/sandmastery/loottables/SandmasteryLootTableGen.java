/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.loottables;

import leaf.cosmere.loottables.BaseLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SandmasteryLootTableGen extends BaseLootProvider
{
	public SandmasteryLootTableGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(packOutput, List.of(
				new SubProviderEntry(SandmasteryBlockLootTableGen::new, LootContextParamSets.BLOCK)
				//,new SubProviderEntry(ExampleEntityLootTableGen::new, LootContextParamSets.ENTITY)
		), registries);
	}
}
