/*
 * File updated ~ 2026-04-26 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
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
		), registries);
	}
}
