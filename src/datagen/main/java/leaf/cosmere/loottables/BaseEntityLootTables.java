/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.loottables;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

public abstract class BaseEntityLootTables extends EntityLootSubProvider
{

	private final Set<EntityType<?>> knownEntityTypes = new ReferenceOpenHashSet<>();

	protected BaseEntityLootTables()
	{
		super(FeatureFlags.VANILLA_SET);
	}

	@Override
	protected void add(@NotNull EntityType<?> type, @NotNull LootTable.Builder table)
	{
		//Overwrite the core register method to add to our list of known entity types
		//Note: This isn't the actual core method as that one takes a ResourceLocation, but all our things wil pass through this one
		super.add(type, table);
		knownEntityTypes.add(type);
	}

	@NotNull
	@Override
	protected Stream<EntityType<?>> getKnownEntityTypes()
	{
		return knownEntityTypes.stream();
	}

	protected void add(@NotNull IEntityTypeProvider typeProvider, @NotNull LootTable.Builder table)
	{
		add(typeProvider.getEntityType(), table);
	}
}