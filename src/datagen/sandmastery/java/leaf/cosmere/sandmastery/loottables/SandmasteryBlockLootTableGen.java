/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.loottables;

import com.mojang.datafixers.kinds.Const;
import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.sandmastery.common.blocks.*;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class SandmasteryBlockLootTableGen extends BlockLoot
{
	@Override
	protected void addTables()
	{
		for (IBlockProvider itemRegistryObject : SandmasteryBlocksRegistry.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();

			if (block instanceof SandJarBlock)
			{
				this.add(block, (jar) -> createSingleItemTable(SandmasteryItems.SAND_JAR_ITEM.asItem()));
			}
			else if (block instanceof TaldainBlackSandLayerBlock || block instanceof TaldainWhiteSandLayerBlock)
			{
				Block blackLayer = SandmasteryBlocksRegistry.TALDAIN_BLACK_SAND_LAYER.getBlock();
				Block whiteLayer = SandmasteryBlocksRegistry.TALDAIN_WHITE_SAND_LAYER.getBlock();
				this.add(SandmasteryBlocksRegistry.TALDAIN_BLACK_SAND_LAYER.getBlock(), (layer) -> LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add(AlternativesEntry.alternatives(TaldainBlackSandLayerBlock.LAYERS.getPossibleValues(), (layers) -> LootItem.lootTableItem(blackLayer.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(layer).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TaldainBlackSandLayerBlock.LAYERS, layers))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(layers.intValue())))))));
				this.add(SandmasteryBlocksRegistry.TALDAIN_WHITE_SAND_LAYER.getBlock(), (layer) -> LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add(AlternativesEntry.alternatives(TaldainWhiteSandLayerBlock.LAYERS.getPossibleValues(), (layers) -> LootItem.lootTableItem(whiteLayer.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(layer).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TaldainWhiteSandLayerBlock.LAYERS, layers))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(layers.intValue())))))));
			}
			else if (block instanceof TemporarySandBlock)
			{
				this.dropOther(block, Items.AIR);
			}
			else
			{
				this.dropSelf(block);
			}
		}
	}

	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return SandmasteryBlocksRegistry.BLOCKS.getAllBlocks().stream().map(IBlockProvider::getBlock)::iterator;
	}
}
