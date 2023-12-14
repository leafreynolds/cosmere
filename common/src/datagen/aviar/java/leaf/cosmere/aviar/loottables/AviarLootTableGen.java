/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.loottables;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AviarLootTableGen extends LootTableProvider
{
	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> loot_tables = ImmutableList.of(Pair.of(AviarBlockLootTableGen::new, LootContextParamSets.BLOCK));

	public AviarLootTableGen(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables()
	{
		return this.loot_tables;
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker)
	{
	}
}
