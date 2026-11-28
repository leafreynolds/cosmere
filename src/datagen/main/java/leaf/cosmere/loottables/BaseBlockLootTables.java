/*
 * File updated ~ 24 - 6 - 2026 ~ Leaf
 */

package leaf.cosmere.loottables;

import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import leaf.cosmere.api.providers.IBlockProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public abstract class BaseBlockLootTables extends BlockLootSubProvider
{

	private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();
	//Note: We use an array set as we never expect this to have more than a few elements (in reality it only ever has one)
	private final Set<Block> toSkip = new ReferenceArraySet<>();

	protected BaseBlockLootTables(HolderLookup.Provider provider)
	{
		//Note: We manually handle explosion resistance on a case by case basis dynamically
		super(Collections.emptySet(), FeatureFlags.VANILLA_SET, provider);
	}

	@Override
	protected void add(@NotNull Block block, @NotNull LootTable.Builder table)
	{
		//Overwrite the core register method to add to our list of known blocks
		super.add(block, table);
		knownBlocks.add(block);
	}

	@NotNull
	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return knownBlocks;
	}

	protected void skip(IBlockProvider... blockProviders)
	{
		for (IBlockProvider blockProvider : blockProviders)
		{
			toSkip.add(blockProvider.getBlock());
		}
	}

	protected void addToSkip(Block skip)
	{
		toSkip.add(skip);
	}

	protected boolean skipBlock(Block block)
	{
		//Skip any blocks that we already registered a table for or have marked to skip
		return knownBlocks.contains(block) || toSkip.contains(block);
	}

	protected Builder createOreDrop(Block block, ItemLike item)
	{
		HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item.asItem())
				.apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
		));
	}

	protected Builder droppingWithFortuneOrRandomly(Block block, ItemLike item, UniformGenerator range)
	{
		HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item.asItem())
				.apply(SetItemCountFunction.setCount(range))
				.apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
		));
	}

	//IBlockProvider versions of BlockLootTable methods, modified to support varargs
	protected void dropSelf(List<IBlockProvider> blockProviders)
	{
		for (IBlockProvider blockProvider : blockProviders)
		{
			Block block = blockProvider.getBlock();
			if (!skipBlock(block))
			{
				dropSelf(block);
			}
		}
	}

	protected void add(Function<Block, Builder> factory, Collection<? extends IBlockProvider> blockProviders)
	{
		for (IBlockProvider blockProvider : blockProviders)
		{
			add(blockProvider.getBlock(), factory);
		}
	}

	protected void add(Function<Block, Builder> factory, IBlockProvider... blockProviders)
	{
		for (IBlockProvider blockProvider : blockProviders)
		{
			add(blockProvider.getBlock(), factory);
		}
	}


	/**
	 * Like vanilla's {@link BlockLootSubProvider#dropOther(Block, ItemLike)} except with a named pool
	 */
	@Override
	public void dropOther(@NotNull Block block, @NotNull ItemLike drop)
	{
		add(block, createSingleItemTable(drop));
	}

	/**
	 * Like vanilla's {@link BlockLootSubProvider#createSingleItemTable(ItemLike)} except with a named pool
	 */
	@NotNull
	@Override
	public LootTable.Builder createSingleItemTable(@NotNull ItemLike item)
	{
		return LootTable.lootTable().withPool(applyExplosionCondition(item, LootPool.lootPool()
				.name("main")
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(item))
		));
	}

	/**
	 * Like vanilla's {@link BlockLootSubProvider#createSingleItemTableWithSilkTouch(Block, ItemLike, NumberProvider)} except with a named pool
	 */
	@NotNull
	@Override
	protected LootTable.Builder createSingleItemTableWithSilkTouch(@NotNull Block block, @NotNull ItemLike item, @NotNull NumberProvider range)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(range))));
	}

}
