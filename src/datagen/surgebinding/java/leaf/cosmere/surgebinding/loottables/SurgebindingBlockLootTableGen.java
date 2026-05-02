/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.surgebinding.loottables;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.loottables.BaseBlockLootTables;
import leaf.cosmere.surgebinding.common.blocks.GemOreBlock;
import leaf.cosmere.surgebinding.common.blocks.SapphireClusterBlock;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class SurgebindingBlockLootTableGen extends BaseBlockLootTables
{
	public SurgebindingBlockLootTableGen(HolderLookup.Provider provider)
	{
		super(provider);
	}

	@Override
	protected void generate()
	{
		HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

		//first catch any blocks that don't drop self, like ores
		for (IBlockProvider itemRegistryObject : SurgebindingBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			if (block instanceof GemOreBlock oreBlock)
			{
				this.add(oreBlock, (ore) -> createOreDrop(ore, SurgebindingItems.GEMSTONE.get(oreBlock.getGemType()).asItem()));
			}
			if (block == SurgebindingBlocks.SAPPHIRE_CLUSTER.getBlock())
			{
				this.add(block, (cluster) -> createSilkTouchDispatchTable(
						cluster,
						LootItem.lootTableItem(SurgebindingItems.GEMSTONE.get(Roshar.Gemstone.SAPPHIRE).asItem())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
								.apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
								.when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
								.otherwise(this.applyExplosionDecay(
										cluster,
										LootItem.lootTableItem(SurgebindingItems.GEMSTONE.get(Roshar.Gemstone.SAPPHIRE).asItem())
												.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
								))
				));
			}
			if (block instanceof SapphireClusterBlock && block != SurgebindingBlocks.SAPPHIRE_CLUSTER.getBlock())
			{
				this.addToSkip(block);
			}
			if (block == SurgebindingBlocks.BUDDING_SAPPHIRE.getBlock())
			{
				this.addToSkip(block);
			}
		}

		//then make the rest drop themselves.
		dropSelf(SurgebindingBlocks.BLOCKS.getAllBlocks());
	}
}
