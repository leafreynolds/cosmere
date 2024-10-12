/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.loottables;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.loottables.BaseBlockLootTables;
import leaf.cosmere.sandmastery.common.blocks.SandJarBlock;
import leaf.cosmere.sandmastery.common.blocks.TaldainBlackSandLayerBlock;
import leaf.cosmere.sandmastery.common.blocks.TaldainWhiteSandLayerBlock;
import leaf.cosmere.sandmastery.common.blocks.TemporarySandBlock;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocks;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class SandmasteryBlockLootTableGen extends BaseBlockLootTables
{
	@Override
	protected void generate()
	{
		for (IBlockProvider itemRegistryObject : SandmasteryBlocks.BLOCKS.getAllBlocks())
		{
			final Block block = itemRegistryObject.getBlock();

			if (block instanceof SandJarBlock)
			{
				this.add(block, (jar) -> createSingleItemTable(SandmasteryItems.SAND_JAR_ITEM.asItem()));
			}
			else if (block instanceof TaldainBlackSandLayerBlock || block instanceof TaldainWhiteSandLayerBlock)
			{
				Block blackLayer = SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock();
				Block whiteLayer = SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock();
				this.add(SandmasteryBlocks.TALDAIN_BLACK_SAND_LAYER.getBlock(), (layer) -> LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add(AlternativesEntry.alternatives(TaldainBlackSandLayerBlock.LAYERS.getPossibleValues(), (layers) -> LootItem.lootTableItem(blackLayer.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(layer).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TaldainBlackSandLayerBlock.LAYERS, layers))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(layers.intValue())))))));
				this.add(SandmasteryBlocks.TALDAIN_WHITE_SAND_LAYER.getBlock(), (layer) -> LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add(AlternativesEntry.alternatives(TaldainWhiteSandLayerBlock.LAYERS.getPossibleValues(), (layers) -> LootItem.lootTableItem(whiteLayer.asItem()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(layer).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TaldainWhiteSandLayerBlock.LAYERS, layers))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(layers.intValue())))))));
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
		//todo delete this when moving generate function to use BaseBlockLootTables functions
		return SandmasteryBlocks.BLOCKS.getAllBlocks().stream().map(IBlockProvider::getBlock)::iterator;
	}
}
