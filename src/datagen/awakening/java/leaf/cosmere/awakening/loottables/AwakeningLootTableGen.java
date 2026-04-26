/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.awakening.loottables;

import leaf.cosmere.loottables.BaseLootProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AwakeningLootTableGen extends BaseLootProvider
{
	public AwakeningLootTableGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(packOutput, List.of(
				new SubProviderEntry(AwakeningBlockLootTableGen::new, LootContextParamSets.BLOCK)
				//,new SubProviderEntry(ExampleEntityLootTableGen::new, LootContextParamSets.ENTITY)
		), registries);
	}
}
