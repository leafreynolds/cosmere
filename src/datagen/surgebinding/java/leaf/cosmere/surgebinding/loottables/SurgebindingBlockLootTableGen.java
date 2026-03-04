/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.loottables;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.loottables.BaseBlockLootTables;
import leaf.cosmere.surgebinding.common.blocks.GemOreBlock;
import leaf.cosmere.surgebinding.common.blocks.SapphireClusterBlock;
import leaf.cosmere.surgebinding.common.registries.SurgebindingBlocks;
import leaf.cosmere.surgebinding.common.registries.SurgebindingItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public class SurgebindingBlockLootTableGen extends BaseBlockLootTables
{
	@Override
	protected void generate()
	{
		//first catch any blocks that don't drop self, like ores
		for (IBlockProvider itemRegistryObject : SurgebindingBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();
			if (block instanceof GemOreBlock oreBlock)
			{
				this.add(oreBlock, (ore) -> createOreDrop(ore, SurgebindingItems.GEMSTONE.get(oreBlock.getGemType())));
			}
			if(block==SurgebindingBlocks.SAPPHIRE_CLUSTER.getBlock())
			{
				this.add(block,(cluster)-> createSilkTouchDispatchTable(cluster, LootItem.lootTableItem(SurgebindingItems.GEMSTONE.get(Roshar.Gemstone.SAPPHIRE)).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(cluster, LootItem.lootTableItem(SurgebindingItems.GEMSTONE.get(Roshar.Gemstone.SAPPHIRE)).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
			}
			if(block instanceof SapphireClusterBlock clusterBlock && block!=SurgebindingBlocks.SAPPHIRE_CLUSTER.getBlock()){
				this.addToSkip(block);
			}
			if(block == SurgebindingBlocks.BUDDING_SAPPHIRE.getBlock()){
				this.addToSkip(block);
			}
		}

		//then make the rest drop themselves.
		dropSelf(SurgebindingBlocks.BLOCKS.getAllBlocks());
	}
}
