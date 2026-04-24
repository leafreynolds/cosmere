/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.loottables;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class BaseLootProvider extends LootTableProvider
{

	protected BaseLootProvider(PackOutput output, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries)
	{
		this(output, Collections.emptySet(), subProviders, registries);
	}

	protected BaseLootProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, requiredTables, subProviders, registries);
	}
}
