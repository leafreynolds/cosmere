/*
 * File updated ~ 7 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.loottables;

import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import leaf.cosmere.api.providers.IBlockProvider;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
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

	private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item()
			.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

	private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();
	//Note: We use an array set as we never expect this to have more than a few elements (in reality it only ever has one)
	private final Set<Block> toSkip = new ReferenceArraySet<>();

	protected BaseBlockLootTables()
	{
		//Note: We manually handle explosion resistance on a case by case basis dynamically
		super(Collections.emptySet(), FeatureFlags.VANILLA_SET);
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

	protected boolean skipBlock(Block block)
	{
		//Skip any blocks that we already registered a table for or have marked to skip
		return knownBlocks.contains(block) || toSkip.contains(block);
	}

	protected Builder createOreDrop(Block block, ItemLike item)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(item.asItem())
				.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
		));
	}

	protected Builder droppingWithFortuneOrRandomly(Block block, ItemLike item, UniformGenerator range)
	{
		return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(item.asItem())
				.apply(SetItemCountFunction.setCount(range))
				.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
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
	 * Like vanilla's {@link BlockLootSubProvider#applyExplosionCondition(ItemLike, ConditionUserBuilder)} except with a boolean for if it is explosion resistant.
	 */
	private static <T extends ConditionUserBuilder<T>> T applyExplosionCondition(boolean explosionResistant, ConditionUserBuilder<T> condition)
	{
		return explosionResistant ? condition.unwrap() : condition.when(ExplosionCondition.survivesExplosion());
	}

	/**
	 * Like vanilla's {@link BlockLootSubProvider#createSlabItemTable(Block)} except with a named pool
	 */
	@NotNull
	@Override
	protected LootTable.Builder createSlabItemTable(@NotNull Block slab)
	{
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.name("main")
				.setRolls(ConstantValue.exactly(1))
				.add(applyExplosionDecay(slab, LootItem.lootTableItem(slab)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
										.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
												.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
								)
						)
				)
		);
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

	/**
	 * Like vanilla's {@link BlockLootSubProvider#createSilkTouchDispatchTable(Block, LootPoolEntryContainer.Builder)} except with a named pool
	 */
	@NotNull
	protected static LootTable.Builder createSilkTouchDispatchTable(@NotNull Block block, @NotNull LootPoolEntryContainer.Builder<?> builder)
	{
		return createSelfDropDispatchTable(block, HAS_SILK_TOUCH, builder);
	}

	/**
	 * Like vanilla's {@link BlockLootSubProvider#createSelfDropDispatchTable(Block, LootItemCondition.Builder, LootPoolEntryContainer.Builder)} except with a named pool
	 */
	@NotNull
	protected static LootTable.Builder createSelfDropDispatchTable(@NotNull Block block, @NotNull LootItemCondition.Builder conditionBuilder,
	                                                               @NotNull LootPoolEntryContainer.Builder<?> entry)
	{
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.name("main")
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(block)
						.when(conditionBuilder)
						.otherwise(entry)
				)
		);
	}

}